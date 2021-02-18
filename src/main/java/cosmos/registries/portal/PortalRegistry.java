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
public class PortalRegistry implements CosmosRegistry<LocatableBlock, CosmosPortal> {

    private final Map<LocatableBlock, CosmosPortal> portalMap = new HashMap<>();
    private final Map<ResourceKey, CosmosPortal> portalMapByKey = new HashMap<>();

    public Optional<CosmosPortal> find(final ResourceKey key) {
        return Optional.ofNullable(this.value(key));
    }

    public boolean has(final ResourceKey key) {
        return this.find(key).isPresent();
    }

    @Override
    public Optional<CosmosRegistryEntry<LocatableBlock, CosmosPortal>> register(final LocatableBlock key, final CosmosPortal value) {
        return Optional.ofNullable(this.portalMap.computeIfAbsent(key, k -> value))
                .map(v -> {
                    this.portalMapByKey.putIfAbsent(v.getKey(), v);

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public boolean replace(final CosmosPortal value) {
        return this.portalMapByKey.replace(value.getKey(), value) != null
                && value.getOrigins()
                .stream()
                .map(ServerLocation::asLocatableBlock)
                .allMatch(key -> this.portalMap.replace(key, value) != null);
    }

    @Override
    public Stream<CosmosPortal> stream() {
        return this.portalMapByKey.values().stream();
    }

    @Override
    public Optional<CosmosRegistryEntry<LocatableBlock, CosmosPortal>> unregister(final LocatableBlock key) {
        return Optional.ofNullable(this.portalMap.remove(key))
                .map(v -> {
                    if (!this.portalMap.containsValue(v)) {
                        this.portalMapByKey.remove(v.getKey(), v);
                    }

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public Optional<CosmosRegistryEntry<ResourceKey, CosmosPortal>> unregister(final ResourceKey key) {
        return Optional.ofNullable(this.portalMapByKey.remove(key))
                .map(v -> {
                    v.getOrigins().forEach(location -> this.portalMap.remove(location.asLocatableBlock()));

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    @Override
    public CosmosPortal value(final LocatableBlock key) {
        return this.portalMap.get(key);
    }

    public CosmosPortal value(final ResourceKey key) {
        return this.portalMapByKey.get(key);
    }

}
