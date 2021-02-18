package cosmos.services.world.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
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
import org.spongepowered.api.command.parameter.Parameter;
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

    private final BackupService backupService;
    private final MessageService messageService;
    private final TransportationService transportationService;

    @Inject
    public WorldServiceImpl(final Injector injector) {
        this.backupService = injector.getInstance(BackupService.class);
        this.messageService = injector.getInstance(MessageService.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    @Override
    public void copy(final Audience src, final ResourceKey worldKey, final ResourceKey copyKey, final boolean checkNonexistent) throws CommandException {
        if (checkNonexistent && Sponge.server().worldManager().worldExists(copyKey)) {
            throw this.messageService.getError(src, "error.world.already-existing", "world", copyKey);
        }

        try {
            if (!Sponge.server().worldManager().copyWorld(worldKey, copyKey).join()) {
                throw this.messageService.getMessage(src, "error.root.copy")
                        .replace("copy", copyKey)
                        .replace("world", worldKey)
                        .asError();
            }
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while duplicating world", e);
            throw this.messageService.getMessage(src, "error.root.copy")
                    .replace("copy", copyKey)
                    .replace("world", worldKey)
                    .asError();
        }
    }

    @Override
    public void delete(final Audience src, final ResourceKey worldKey, final boolean checkOffline) throws CommandException {
        if (checkOffline && this.isOnline(worldKey)) {
            throw this.messageService.getError(src, "error.world.already-loaded", "world", worldKey);
        }

        try {
            if (!Sponge.server().worldManager().deleteWorld(worldKey).join()) {
                throw this.messageService.getError(src, "error.root.delete", "world", worldKey);
            }
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while deleting world", e);
            throw this.messageService.getError(src, "error.root.delete", "world", worldKey);
        }
    }

    @Override
    public Optional<ResourceKey> findKeyOrSource(final CommandContext context) {
        return this.findKeyOrSource(context, CosmosKeys.WORLD);
    }

    @Override
    public Optional<ResourceKey> findKeyOrSource(final CommandContext context, final Parameter.Key<ResourceKey> worldKey) {
        return context.one(worldKey)
                .map(Optional::of)
                .orElse(this.locateSourceKey(context.cause().audience()));
    }

    @Override
    public ResourceKey keyOrSource(final CommandContext context) throws CommandException {
        return this.keyOrSource(context, CosmosKeys.WORLD);
    }

    @Override
    public ResourceKey keyOrSource(final CommandContext context, final Parameter.Key<ResourceKey> worldKey) throws CommandException {
        return this.findKeyOrSource(context, worldKey)
                .orElseThrow(this.messageService.getMessage(context, "error.missing.world.any").asSupplier());
    }

    @Override
    public ServerWorldProperties propertiesOrSource(final CommandContext context) throws CommandException {
        return context.one(CosmosKeys.WORLD)
                .map(this::loadProperties)
                .orElse(this.locateSourceProperties(context.cause().audience()))
                .orElseThrow(this.messageService.getMessage(context, "error.missing.world.properties").asSupplier());
    }

    // TODO https://github.com/SpongePowered/Sponge/issues/3268
    @Override
    public boolean isImported(final ResourceKey worldKey) {
        return true; //Sponge.server().worldManager().templateExists(worldKey);
    }

    @Override
    public boolean isOnline(final ResourceKey worldKey) {
        return this.isImported(worldKey) && Sponge.server().worldManager().world(worldKey).isPresent();
    }

    @Override
    public boolean isOffline(final ResourceKey worldKey) {
        final WorldManager worldManager = Sponge.server().worldManager();
        return this.isImported(worldKey) && worldManager.worldExists(worldKey) && !worldManager.world(worldKey).isPresent();
    }

    @Override
    public void load(final Audience src, final ResourceKey worldKey, final boolean checkOffline) throws CommandException {
        if (checkOffline && this.isOnline(worldKey)) {
            throw this.messageService.getError(src, "error.world.already-loaded", "world", worldKey);
        }

        try {
            Sponge.server().worldManager().loadWorld(worldKey).join().properties().setLoadOnStartup(true);
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while loading world", e);
            throw this.messageService.getError(src, "error.root.load", "world", worldKey);
        }
    }

    private Optional<ServerWorldProperties> loadProperties(final ResourceKey worldKey) {
        if (this.isOnline(worldKey)) {
            return Sponge.server().worldManager().world(worldKey).map(ServerWorld::properties);
        }

        try {
            return Sponge.server().worldManager().loadProperties(worldKey).join();
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    private Optional<ResourceKey> locateSourceKey(final Audience src) {
        return this.locateSourceWorld(src).map(ServerWorld::key);
    }

    private Optional<ServerWorldProperties> locateSourceProperties(final Audience src) {
        return this.locateSourceWorld(src).map(ServerWorld::properties);
    }

    private Optional<ServerWorld> locateSourceWorld(final Audience src) {
        return Optional.ofNullable(src instanceof Locatable ? ((Locatable) src).serverLocation().world() : null);
    }

    @Override
    public void rename(final Audience src, final ResourceKey worldKey, final ResourceKey renameKey, final boolean checkNonexistent) throws CommandException {
        if (checkNonexistent && Sponge.server().worldManager().worldExists(renameKey)) {
            throw this.messageService.getError(src, "error.world.already-existing", "world", renameKey);
        }

        try {
            if (!Sponge.server().worldManager().moveWorld(worldKey, renameKey).join()) {
                throw this.messageService.getMessage(src, "error.root.rename")
                        .replace("rename", renameKey)
                        .replace("world", worldKey)
                        .asError();
            }
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while renaming world", e);
            throw this.messageService.getMessage(src, "error.root.rename")
                    .replace("rename", renameKey)
                    .replace("world", worldKey)
                    .asError();
        }
    }

    @Override
    public void restore(final Audience src, final BackupArchetype backupArchetype, final boolean checkExistent) throws CommandException {
        final ResourceKey defaultWorldKey = Sponge.server().worldManager().defaultWorld().key();
        final ResourceKey worldKey = backupArchetype.worldKey();

        if (defaultWorldKey.equals(worldKey)) {
            throw this.messageService.getError(src, "error.backup.restore.default", "world", defaultWorldKey);
        }

        if (!this.isImported(worldKey)) {
            throw this.messageService.getError(src, "error.backup.restore.exported", "world", worldKey);
        }

        if (this.isOnline(worldKey)) {
            throw this.messageService.getError(src, "error.backup.restore.loaded", "world", worldKey);
        }

        if (checkExistent && !this.backupService.hasBackup(backupArchetype.worldKey())) {
            throw this.messageService.getError(src, "error.missing.backup", "backup", backupArchetype);
        }

        try {
            this.backupService.restore(backupArchetype);
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while restoring backup", e);
            throw this.messageService.getError(src, "error.backup.restore", "backup", backupArchetype);
        }
    }

    @Override
    public void saveProperties(final Audience src, final ServerWorldProperties properties) throws CommandException {
        if (this.isOnline(properties.key())) {
            return;
        }

        boolean success = false;

        try {
            success = Sponge.server().worldManager().saveProperties(properties).join();
        } catch (final Exception e) {
            Cosmos.logger().error("An error occurred while saving world properties", e);
        }

        if (!success) {
            throw this.messageService.getError(src, "error.properties.save", "world", properties);
        }
    }

    @Override
    public void unload(final Audience src, final ResourceKey worldKey, final boolean checkOnline) throws CommandException {
        if (checkOnline && this.isOffline(worldKey)) {
            throw this.messageService.getError(src, "error.world.already-unloaded", "world", worldKey);
        }

        final ServerWorld world = Sponge.server().worldManager().world(worldKey)
                .orElseThrow(this.messageService.supplyError(src, "error.missing.world", "world", worldKey));

        this.unload(src, world);
    }

    @Override
    public void unload(final Audience src, final ServerWorld world) throws CommandException {
        final ServerWorld defaultWorld = Sponge.server().worldManager().defaultWorld();

        if (defaultWorld.key().equals(world.key())) {
            throw this.messageService.getError(src, "error.root.unload.default", "world", world);
        }

        final ServerLocation defaultSpawnLocation = defaultWorld.location(defaultWorld.properties().spawnPosition());

        if (!world.players().stream().allMatch(player -> this.transportationService.teleport(player, defaultSpawnLocation, false))) {
            throw this.messageService.getError(src, "error.root.unload.remaining-players", "world", world);
        }

        final ServerWorldProperties properties = world.properties();
        final boolean loadOnStartupFallback = properties.loadOnStartup();

        try {
            properties.setLoadOnStartup(false);
            Sponge.server().worldManager().unloadWorld(world).join();
        } catch (final Exception e) {
            properties.setLoadOnStartup(loadOnStartupFallback);
            Cosmos.logger().error("An unexpected error occurred while unloading world", e);
            throw this.messageService.getError(src, "error.root.unload", "world", world);
        }
    }

    @Override
    public Collection<ResourceKey> worldKeysOffline() {
        return Sponge.server().worldManager().worldKeys()
                .stream()
                .filter(this::isOffline)
                .collect(Collectors.toList());
    }

}
