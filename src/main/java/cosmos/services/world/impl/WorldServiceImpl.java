package cosmos.services.world.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.models.backup.BackupArchetype;
import cosmos.models.parameters.CosmosKeys;
import cosmos.services.io.BackupService;
import cosmos.services.io.FinderService;
import cosmos.services.message.MessageService;
import cosmos.services.transportation.TransportationService;
import cosmos.services.world.WorldService;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class WorldServiceImpl implements WorldService {

    @Inject
    private BackupService backupService;

    @Inject
    private FinderService finderService;

    @Inject
    private MessageService messageService;

    @Inject
    private TransportationService transportationService;

    @Override
    public Optional<ServerWorldProperties> findProperties(final Audience src) {
        return Optional.ofNullable(src instanceof Locatable ? ((Locatable) src).getServerLocation().getWorld().getProperties() : null);
    }

    @Override
    public ServerWorldProperties getProperties(final CommandContext context) throws CommandException {
        return context.getOne(CosmosKeys.WORLD)
                .map(Optional::of)
                .orElse(this.findProperties(context.getCause().getAudience()))
                .orElseThrow(this.messageService.getMessage(context, "error.missing.world.properties").asSupplier());
    }

    @Override
    public boolean isImported(final ResourceKey worldKey) {
        return this.finderService.getWorldSpongeDataPath(worldKey)
                .map(this.finderService::doesFileExist)
                .orElse(false);
    }

    @Override
    public boolean isLoaded(final ResourceKey worldKey) {
        return Sponge.getServer().getWorldManager().getWorlds()
                .stream()
                .map(ServerWorld::getKey)
                .anyMatch(worldKey::equals);
    }

    @Override
    public boolean isLoaded(final ServerWorldProperties properties) {
        return this.isLoaded(properties.getKey());
    }

    @Override
    public void load(final Audience src, final ServerWorldProperties properties, final boolean verifyLoaded) throws CommandException {
        if (verifyLoaded && Sponge.getServer().getWorldManager().getWorld(properties.getKey()).isPresent()) {
            throw this.messageService.getMessage(src, "error.root.load.loaded")
                    .replace("world", properties)
                    .errorColor()
                    .asException();
        }

        try {
            Sponge.getServer().getWorldManager().loadWorld(properties).join();
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while loading world", e);
            throw this.messageService.getMessage(src, "error.root.load")
                    .replace("world", properties)
                    .errorColor()
                    .asException();
        }

        properties.setLoadOnStartup(true);
        properties.setEnabled(true);

        this.saveProperties(src, properties);
    }

    @Override
    public void load(final Audience src, final ResourceKey worldKey, final boolean verifyLoaded) throws CommandException {
        final ServerWorldProperties properties = Sponge.getServer().getWorldManager().getProperties(worldKey)
                .orElseThrow(this.messageService.getMessage(src, "error.missing.world").replace("world", worldKey).errorColor().asSupplier());

        this.load(src, properties, verifyLoaded);
    }

    @Override
    public void restore(final Audience src, final BackupArchetype backupArchetype, final boolean verifyExistence) throws CommandException {
        final ResourceKey defaultWorldKey = Sponge.getServer().getDefaultWorldKey();
        final ResourceKey worldKey = backupArchetype.getWorldKey();

        if (defaultWorldKey.equals(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.default")
                    .replace("world", defaultWorldKey)
                    .errorColor()
                    .asException();
        }

        if (!this.isImported(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.exported")
                    .replace("world", worldKey)
                    .errorColor()
                    .asException();
        }

        if (this.isLoaded(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.loaded")
                    .replace("world", worldKey)
                    .errorColor()
                    .asException();
        }

        if (verifyExistence && !this.backupService.hasBackup(backupArchetype.getUuid())) {
            throw this.messageService.getMessage(src, "error.missing.backup")
                    .replace("backup", backupArchetype)
                    .errorColor()
                    .asException();
        }

        try {
            this.backupService.restore(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occured while restoring backup", e);
            throw this.messageService.getMessage(src, "error.backup.restore")
                    .replace("backup", backupArchetype)
                    .errorColor()
                    .asException();
        }
    }

    @Override
    public void saveProperties(final Audience src, final ServerWorldProperties properties) throws CommandException {
        try {
            Sponge.getServer().getWorldManager().saveProperties(properties).join();
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An error occurred while saving world properties", e);
            throw this.messageService
                    .getMessage(src, "error.properties.save")
                    .replace("world", properties)
                    .errorColor()
                    .asException();
        }
    }

    @Override
    public void unload(final Audience src, final ServerWorldProperties properties, final boolean verifyLoaded) throws CommandException {
        if (verifyLoaded && !this.isLoaded(properties)) {
            throw this.messageService.getMessage(src, "error.root.unload.unloaded")
                    .replace("world", properties)
                    .errorColor()
                    .asException();
        }

        final ResourceKey defaultWorldKey = Sponge.getServer().getDefaultWorldKey();
        final ResourceKey worldKey = properties.getKey();

        if (defaultWorldKey.equals(worldKey)) {
            throw this.messageService.getMessage(src, "error.root.unload.default")
                    .replace("world", properties)
                    .errorColor()
                    .asException();
        }

        final ServerWorld defaultWorld = Sponge.getServer().getWorldManager().getWorld(defaultWorldKey)
                .orElseThrow(this.messageService.getMessage(src, "error.missing.world.default").replace("world", defaultWorldKey).errorColor().asSupplier());
        final ServerWorld world = Sponge.getServer().getWorldManager().getWorld(worldKey)
                .orElseThrow(this.messageService.getMessage(src, "error.missing.world").replace("world", worldKey).errorColor().asSupplier());

        final ServerLocation defaultSpawnLocation = defaultWorld.getLocation(defaultWorld.getProperties().getSpawnPosition());

        if (!world.getPlayers().stream().allMatch(player -> this.transportationService.teleport(player, defaultSpawnLocation, false))) {
            throw this.messageService.getMessage(src, "error.root.unload.remaining-players")
                    .replace("world", properties)
                    .errorColor()
                    .asException();
        }

        try {
            Sponge.getServer().getWorldManager().unloadWorld(world).join();
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while unloading world", e);
            throw this.messageService.getMessage(src, "error.root.unload")
                    .replace("world", properties)
                    .errorColor()
                    .asException();
        }

        properties.setLoadOnStartup(false);

        this.saveProperties(src, properties);
    }
}
