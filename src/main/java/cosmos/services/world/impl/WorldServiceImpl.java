package cosmos.services.world.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.io.BackupService;
import cosmos.services.message.MessageService;
import cosmos.services.transportation.TransportationService;
import cosmos.services.world.WorldService;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.WorldManager;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class WorldServiceImpl implements WorldService {

    @Inject
    private BackupService backupService;

    @Inject
    private MessageService messageService;

    @Inject
    private TransportationService transportationService;

    @Override
    public void copy(final Audience src, final ResourceKey worldKey, final ResourceKey copyKey, final boolean checkNonexistent) throws CommandException {
        if (checkNonexistent && Sponge.getServer().getWorldManager().worldExists(copyKey)) {
            throw this.messageService.getMessage(src, "error.world.already-existing")
                    .replace("world", copyKey)
                    .asException();
        }

        final CommandException errorException = this.messageService.getMessage(src, "error.root.copy")
                .replace("copy", copyKey)
                .replace("world", worldKey)
                .asException();

        try {
            if (!Sponge.getServer().getWorldManager().copyWorld(worldKey, copyKey).join()) {
                throw errorException;
            }
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while duplicating world", e);
            throw errorException;
        }
    }

    @Override
    public void delete(final Audience src, final ResourceKey worldKey, final boolean checkOffline) throws CommandException {
        if (checkOffline && this.isOnline(worldKey)) {
            throw this.messageService.getMessage(src, "error.world.already-loaded")
                    .replace("world", worldKey)
                    .asException();
        }

        final CommandException errorException = this.messageService.getMessage(src, "error.root.delete")
                .replace("world", worldKey)
                .asException();

        try {
            if (!Sponge.getServer().getWorldManager().deleteWorld(worldKey).join()) {
                throw errorException;
            }
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while deleting world", e);
            throw errorException;
        }
    }

    @Override
    public Optional<ResourceKey> findKeyOrSource(final CommandContext context) {
        return context.getOne(CosmosKeys.WORLD_KEY)
                .map(Optional::of)
                .orElse(this.locateSourceKey(context.getCause().getAudience()));
    }

    @Override
    public ResourceKey getKeyOrSource(final CommandContext context) throws CommandException {
        return this.findKeyOrSource(context)
                .orElseThrow(this.messageService.getMessage(context, "error.missing.world.key").asSupplier());
    }

    @Override
    public ServerWorldProperties getPropertiesOrSource(final CommandContext context) throws CommandException {
        return context.getOne(CosmosKeys.WORLD_KEY)
                .map(this::loadProperties)
                .orElse(this.locateSourceProperties(context.getCause().getAudience()))
                .orElseThrow(this.messageService.getMessage(context, "error.missing.world.properties").asSupplier());
    }

    @Override
    public boolean isImported(final ResourceKey worldKey) {
        return Sponge.getServer().getWorldManager().templateExists(worldKey);
    }

    @Override
    public boolean isOnline(final ResourceKey worldKey) {
        return this.isImported(worldKey) && Sponge.getServer().getWorldManager().world(worldKey).isPresent();
    }

    @Override
    public boolean isOffline(final ResourceKey worldKey) {
        final WorldManager worldManager = Sponge.getServer().getWorldManager();
        return this.isImported(worldKey) && worldManager.worldExists(worldKey) && !worldManager.world(worldKey).isPresent();
    }

    @Override
    public void load(final Audience src, final ResourceKey worldKey, final boolean checkOffline) throws CommandException {
        if (checkOffline && this.isOnline(worldKey)) {
            throw this.messageService.getMessage(src, "error.world.already-loaded")
                    .replace("world", worldKey)
                    .asException();
        }

        try {
            Sponge.getServer().getWorldManager().loadWorld(worldKey).join().getProperties().setLoadOnStartup(true);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while loading world", e);
            throw this.messageService.getMessage(src, "error.root.load")
                    .replace("world", worldKey)
                    .asException();
        }
    }

    private Optional<ServerWorldProperties> loadProperties(final ResourceKey worldKey) {
        if (this.isOnline(worldKey)) {
            return Sponge.getServer().getWorldManager().world(worldKey).map(ServerWorld::getProperties);
        }

        try {
            return Sponge.getServer().getWorldManager().loadProperties(worldKey).join();
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    private Optional<ResourceKey> locateSourceKey(final Audience src) {
        return this.locateSourceWorld(src).map(ServerWorld::getKey);
    }

    private Optional<ServerWorldProperties> locateSourceProperties(final Audience src) {
        return this.locateSourceWorld(src).map(ServerWorld::getProperties);
    }

    private Optional<ServerWorld> locateSourceWorld(final Audience src) {
        return Optional.ofNullable(src instanceof Locatable ? ((Locatable) src).getServerLocation().getWorld() : null);
    }

    @Override
    public void rename(final Audience src, final ResourceKey worldKey, final ResourceKey renameKey, final boolean checkNonexistent) throws CommandException {
        if (checkNonexistent && Sponge.getServer().getWorldManager().worldExists(renameKey)) {
            throw this.messageService.getMessage(src, "error.world.already-existing")
                    .replace("world", renameKey)
                    .asException();
        }

        final CommandException errorException = this.messageService.getMessage(src, "error.root.rename")
                .replace("rename", renameKey)
                .replace("world", worldKey)
                .asException();

        try {
            if (!Sponge.getServer().getWorldManager().moveWorld(worldKey, renameKey).join()) {
                throw errorException;
            }
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while renaming world", e);
            throw errorException;
        }
    }

    @Override
    public void restore(final Audience src, final BackupArchetype backupArchetype, final boolean checkExistent) throws CommandException {
        final ResourceKey defaultWorldKey = Sponge.getServer().getWorldManager().defaultWorld().getKey();
        final ResourceKey worldKey = backupArchetype.getWorldKey();

        if (defaultWorldKey.equals(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.default")
                    .replace("world", defaultWorldKey)
                    .asException();
        }

        if (!this.isImported(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.exported")
                    .replace("world", worldKey)
                    .asException();
        }

        if (this.isOnline(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.loaded")
                    .replace("world", worldKey)
                    .asException();
        }

        if (checkExistent && !this.backupService.hasBackup(backupArchetype.getWorldKey())) {
            throw this.messageService.getMessage(src, "error.missing.backup")
                    .replace("backup", backupArchetype)
                    .asException();
        }

        try {
            this.backupService.restore(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while restoring backup", e);
            throw this.messageService.getMessage(src, "error.backup.restore")
                    .replace("backup", backupArchetype)
                    .asException();
        }
    }

    @Override
    public void saveProperties(final Audience src, final ServerWorldProperties properties) throws CommandException {
        if (this.isOnline(properties.getKey())) {
            return;
        }

        boolean success = false;

        try {
            success = Sponge.getServer().getWorldManager().saveProperties(properties).join();
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An error occurred while saving world properties", e);
        }

        if (!success) {
            throw this.messageService
                    .getMessage(src, "error.properties.save")
                    .replace("world", properties)
                    .asException();
        }
    }

    @Override
    public void unload(final Audience src, final ResourceKey worldKey, final boolean checkOnline) throws CommandException {
        if (checkOnline && this.isOffline(worldKey)) {
            throw this.messageService.getMessage(src, "error.world.already-unloaded")
                    .replace("world", worldKey)
                    .asException();
        }

        final ServerWorld world = Sponge.getServer().getWorldManager().world(worldKey)
                .orElseThrow(
                        this.messageService.getMessage(src, "error.missing.world")
                                .replace("world", worldKey)
                                .asSupplier()
                );

        this.unload(src, world);
    }

    @Override
    public void unload(final Audience src, final ServerWorld world) throws CommandException {
        final ServerWorld defaultWorld = Sponge.getServer().getWorldManager().defaultWorld();

        if (defaultWorld.getKey().equals(world.getKey())) {
            throw this.messageService.getMessage(src, "error.root.unload.default")
                    .replace("world", world)
                    .asException();
        }

        final ServerLocation defaultSpawnLocation = defaultWorld.getLocation(defaultWorld.getProperties().spawnPosition());

        if (!world.getPlayers().stream().allMatch(player -> this.transportationService.teleport(player, defaultSpawnLocation, false))) {
            throw this.messageService.getMessage(src, "error.root.unload.remaining-players")
                    .replace("world", world)
                    .asException();
        }

        final ServerWorldProperties properties = world.getProperties();
        final boolean loadOnStartupFallback = properties.loadOnStartup();

        try {
            properties.setLoadOnStartup(false);
            Sponge.getServer().getWorldManager().unloadWorld(world).join();
        } catch (final Exception e) {
            properties.setLoadOnStartup(loadOnStartupFallback);
            Cosmos.getLogger().warn("An unexpected error occurred while unloading world", e);
            throw this.messageService.getMessage(src, "error.root.unload")
                    .replace("world", world)
                    .asException();
        }
    }

    @Override
    public Collection<ResourceKey> worldKeysOffline() {
        return Sponge.getServer().getWorldManager().worldKeys()
                .stream()
                .filter(this::isOffline)
                .collect(Collectors.toList());
    }
}
