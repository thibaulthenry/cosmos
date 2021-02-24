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
public class PortalRegistry implements CosmosRegistry<ResourceKey, CosmosPortal> {

    private final Map<ResourceKey, CosmosPortal> portalMap = new HashMap<>();
    private final Map<LocatableBlock, CosmosPortal> portalTriggerMap = new HashMap<>();

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
                    v.origins().forEach(location -> this.portalTriggerMap.putIfAbsent(location.asLocatableBlock(), value));
                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public boolean replace(final CosmosPortal value) {
        return this.portalMap.replace(value.key(), value) != null
                && value.origins()
                .stream()
                .map(ServerLocation::asLocatableBlock)
                .allMatch(key -> this.portalTriggerMap.replace(key, value) != null);
    }

    @Override
    public Stream<CosmosPortal> stream() {
        return this.portalMap.values().stream();
    }

    public Optional<CosmosRegistryEntry<LocatableBlock, CosmosPortal>> unregister(final LocatableBlock key) {
        return Optional.ofNullable(this.portalTriggerMap.remove(key))
                .map(v -> {
                    if (!this.portalTriggerMap.containsValue(v)) {
                        this.portalMap.remove(v.key(), v);
                    }

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, CosmosPortal>> unregister(final ResourceKey key) {
        return Optional.ofNullable(this.portalMap.remove(key))
                .map(v -> {
                    v.origins().forEach(location -> this.portalTriggerMap.remove(location.asLocatableBlock()));
                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public CosmosPortal value(final LocatableBlock key) {
        return this.portalTriggerMap.get(key);
    }

    @Override
    public CosmosPortal value(final ResourceKey key) {
        return this.portalMap.get(key);
    }

}
