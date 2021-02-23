package cosmos.registries.portal;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton
public class PortalFrameRegistry implements CosmosRegistry<ResourceKey, CosmosFramePortal> {

    private final Map<ResourceKey, CosmosFramePortal> portalMap = new HashMap<>();
    private final Map<LocatableBlock, CosmosFramePortal> portalTriggerMap = new HashMap<>();

    public Optional<CosmosFramePortal> find(final LocatableBlock key) {
        return Optional.ofNullable(this.value(key));
    }

    public boolean has(final LocatableBlock key) {
        return this.find(key).isPresent();
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, CosmosFramePortal>> register(final ResourceKey key, final CosmosFramePortal value) {
        return Optional.ofNullable(this.portalMap.computeIfAbsent(key, k -> value))
                .map(v -> {
                    v.getOrigins().forEach(location -> this.portalTriggerMap.putIfAbsent(location.asLocatableBlock(), value));
                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public boolean replace(final CosmosFramePortal value) {
        return this.portalMap.replace(value.getKey(), value) != null
                && value.getOrigins()
                .stream()
                .map(ServerLocation::asLocatableBlock)
                .allMatch(key -> this.portalTriggerMap.replace(key, value) != null);
    }

    @Override
    public Stream<CosmosFramePortal> stream() {
        return this.portalMap.values().stream();
    }

    public Optional<CosmosRegistryEntry<LocatableBlock, CosmosFramePortal>> unregister(final LocatableBlock key) {
        return Optional.ofNullable(this.portalTriggerMap.remove(key))
                .map(v -> {
                    if (!this.portalTriggerMap.containsValue(v)) {
                        this.portalMap.remove(v.getKey(), v);
                    }

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, CosmosFramePortal>> unregister(final ResourceKey key) {
        return Optional.ofNullable(this.portalMap.remove(key))
                .map(v -> {
                    v.getOrigins().forEach(location -> this.portalTriggerMap.remove(location.asLocatableBlock()));
                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public CosmosFramePortal value(final LocatableBlock key) {
        return this.portalTriggerMap.get(key);
    }

    @Override
    public CosmosFramePortal value(final ResourceKey key) {
        return this.portalMap.get(key);
    }

}
