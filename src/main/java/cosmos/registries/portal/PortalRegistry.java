package cosmos.registries.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.portal.impl.PortalParticlesTask;
import cosmos.registries.portal.impl.PortalSoundAmbientTask;
import cosmos.registries.serializer.impl.PortalsSerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3i;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton
public class PortalRegistry implements CosmosRegistry<ResourceKey, CosmosPortal> {

    private final Map<ResourceKey, CosmosPortal> portalMap = new HashMap<>();
    private final Map<LocatableBlockType, CosmosPortal> portalTriggerMap = new HashMap<>();

    private final FinderService finderService;
    private final PortalParticlesTaskRegistry portalParticlesTaskRegistry;
    private final PortalSoundAmbientTaskRegistry portalSoundAmbientTaskRegistry;
    private final PortalsSerializer portalsSerializer;

    @Inject
    public PortalRegistry(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.portalParticlesTaskRegistry = injector.getInstance(PortalParticlesTaskRegistry.class);
        this.portalSoundAmbientTaskRegistry = injector.getInstance(PortalSoundAmbientTaskRegistry.class);
        this.portalsSerializer = injector.getInstance(PortalsSerializer.class);
    }

    public Optional<CosmosPortal> find(final LocatableBlock key) {
        return Optional.ofNullable(this.value(key));
    }

    public boolean has(final LocatableBlock key) {
        return this.find(key).isPresent();
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, CosmosPortal>> register(final ResourceKey key, final CosmosPortal value) {
        return Optional.ofNullable(this.portalMap.computeIfAbsent(key, k -> value))
                .map(v -> {
                    v.origins()
                            .stream()
                            .map(ServerLocation::asLocatableBlock)
                            .map(LocatableBlockType::new)
                            .forEach(locatableBlockType -> this.portalTriggerMap.putIfAbsent(locatableBlockType, v));

                    v.particles()
                            .flatMap(effect -> this.portalParticlesTaskRegistry.register(v.key(), new PortalParticlesTask(v)))
                            .map(CosmosRegistryEntry::value)
                            .ifPresent(PortalParticlesTask::submit);

                    v.soundAmbient()
                            .flatMap(sound -> this.portalSoundAmbientTaskRegistry.register(v.key(), new PortalSoundAmbientTask(v)))
                            .map(CosmosRegistryEntry::value)
                            .ifPresent(PortalSoundAmbientTask::submit);

                    this.finderService.findCosmosPath(Directories.PORTALS, v.key())
                            .ifPresent(path -> this.portalsSerializer.serialize(path, v));

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public void registerAll() {
        this.finderService.stream(Directories.PORTALS).forEach(path ->
                this.portalsSerializer.deserialize(path).flatMap(portal -> this.register(portal.key(), portal))
        );
    }

    @Override
    public Stream<CosmosPortal> stream() {
        return this.portalMap.values().stream();
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, CosmosPortal>> unregister(final ResourceKey key) {
        return Optional.ofNullable(this.portalMap.remove(key))
                .map(v -> {
                    v.origins()
                            .stream()
                            .map(ServerLocation::asLocatableBlock)
                            .map(LocatableBlockType::new)
                            .forEach(this.portalTriggerMap::remove);

                    this.portalParticlesTaskRegistry.unregister(v.key())
                            .map(CosmosRegistryEntry::value)
                            .ifPresent(PortalParticlesTask::cancel);

                    this.portalSoundAmbientTaskRegistry.unregister(v.key())
                            .map(CosmosRegistryEntry::value)
                            .ifPresent(PortalSoundAmbientTask::cancel);

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public CosmosPortal value(final LocatableBlock key) {
        return this.portalTriggerMap.get(new LocatableBlockType(key));
    }

    @Override
    public CosmosPortal value(final ResourceKey key) {
        return this.portalMap.get(key);
    }

    private static class LocatableBlockType {

        private final BlockType blockType;
        private final Vector3i position;
        private final ResourceKey world;

        private LocatableBlockType(final LocatableBlock locatableBlock) {
            final ServerLocation serverLocation = locatableBlock.serverLocation();
            this.blockType = locatableBlock.blockState().type();
            this.position = serverLocation.blockPosition();
            this.world = serverLocation.world().key();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            final LocatableBlockType that = (LocatableBlockType) o;

            return com.google.common.base.Objects.equal(this.blockType, that.blockType)
                    && com.google.common.base.Objects.equal(this.position, that.position)
                    && com.google.common.base.Objects.equal(this.world, that.world);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.blockType, this.position, this.world);
        }

    }

}
