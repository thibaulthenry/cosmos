package cosmos.executors.parameters.impl.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

@Singleton
public class WorldOffline implements CosmosBuilder<ResourceKey> {

    private final ValueParameter<ResourceKey> value;

    @Inject
    private WorldOffline(final ServiceProvider serviceProvider) {
        this.value = new WorldFilter(
                "error.invalid.world.offline",
                worldKey -> serviceProvider.world().isOffline(worldKey),
                serviceProvider
        );
    }

    public Parameter.Value.Builder<ResourceKey> builder() {
        return Parameter.builder(ResourceKey.class, this.value).setKey(CosmosKeys.WORLD_KEY);
    }

}
