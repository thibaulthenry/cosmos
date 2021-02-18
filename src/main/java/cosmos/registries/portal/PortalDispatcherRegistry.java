package cosmos.registries.portal;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.portal.impl.PortalDispatcher;
import org.spongepowered.api.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class PortalDispatcherRegistry implements CosmosRegistry<ResourceKey, PortalDispatcher> {

    private final Map<ResourceKey, PortalDispatcher> portalDispatcherMap = new HashMap<>();

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, PortalDispatcher>> register(final ResourceKey key, final PortalDispatcher value) {
        return Optional.ofNullable(this.portalDispatcherMap.computeIfAbsent(key, k -> value)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, PortalDispatcher>> unregister(final ResourceKey key) {
        return Optional.ofNullable(this.portalDispatcherMap.remove(key)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public PortalDispatcher value(final ResourceKey key) {
        return this.portalDispatcherMap.get(key);
    }

}
