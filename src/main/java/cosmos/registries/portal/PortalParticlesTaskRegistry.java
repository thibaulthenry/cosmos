package cosmos.registries.portal;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.portal.impl.PortalParticlesTask;
import org.spongepowered.api.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class PortalParticlesTaskRegistry implements CosmosRegistry<ResourceKey, PortalParticlesTask> {

    private final Map<ResourceKey, PortalParticlesTask> portalParticlesTaskMap = new HashMap<>();

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, PortalParticlesTask>> register(final ResourceKey key, final PortalParticlesTask value) {
        return Optional.ofNullable(this.portalParticlesTaskMap.computeIfAbsent(key, k -> value)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, PortalParticlesTask>> unregister(final ResourceKey key) {
        return Optional.ofNullable(this.portalParticlesTaskMap.remove(key)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public PortalParticlesTask value(final ResourceKey key) {
        return this.portalParticlesTaskMap.get(key);
    }

}
