package cosmos.registries.portal;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.portal.impl.PortalSoundAmbientTask;
import org.spongepowered.api.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class PortalSoundAmbientTaskRegistry implements CosmosRegistry<ResourceKey, PortalSoundAmbientTask> {

    private final Map<ResourceKey, PortalSoundAmbientTask> portalSoundAmbientTaskMap = new HashMap<>();

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, PortalSoundAmbientTask>> register(final ResourceKey key, final PortalSoundAmbientTask value) {
        return Optional.ofNullable(this.portalSoundAmbientTaskMap.computeIfAbsent(key, k -> value)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, PortalSoundAmbientTask>> unregister(final ResourceKey key) {
        return Optional.ofNullable(this.portalSoundAmbientTaskMap.remove(key)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public PortalSoundAmbientTask value(final ResourceKey key) {
        return this.portalSoundAmbientTaskMap.get(key);
    }

}
