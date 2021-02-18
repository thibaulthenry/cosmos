package cosmos.registries.perworld;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import org.spongepowered.api.ResourceKey;

@Singleton
public class RealTimeRegistry implements CosmosRegistry<ResourceKey, Boolean> {

    @Override
    public Boolean value(final ResourceKey key) {
        return null;
    }

}
