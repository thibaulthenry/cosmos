package cosmos.registries.perworld;

import cosmos.registries.CosmosRegistry;
import org.spongepowered.api.ResourceKey;

public class RealTimeRegistry implements CosmosRegistry<ResourceKey, Boolean> {

    @Override
    public Boolean get(ResourceKey key) {
        return null;
    }

    @Override
    public boolean has(ResourceKey key) {
        return false;
    }

}
