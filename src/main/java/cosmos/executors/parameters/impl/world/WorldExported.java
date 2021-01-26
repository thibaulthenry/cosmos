package cosmos.executors.parameters.impl.world;

import com.google.inject.Inject;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class WorldExported implements CosmosBuilder<ResourceKey> {

    private final Parameter.Value.Builder<ResourceKey> builder;

    @Inject
    private WorldExported(final ServiceProvider serviceProvider) {
        final ValueParameter<ResourceKey> value = new WorldFilter(
                src -> serviceProvider.message()
                        .getMessage(src, "error.invalid.world.exported")
                        .replace("parameter", CosmosKeys.WORLD),
                worldKey -> !serviceProvider.world().isImported(worldKey)
        );
        this.builder = Parameter.builder(ResourceKey.class, value);
        this.builder.setKey(CosmosKeys.WORLD);
    }

    @Override
    public Parameter.Value<ResourceKey> build() {
        return this.builder.build();
    }

    public CosmosBuilder<ResourceKey> key(final Parameter.Key<ResourceKey> key) {
        this.builder.setKey(key);
        return this;
    }

    public CosmosBuilder<ResourceKey> key(final String key) {
        return this.key(Parameter.key(key, ResourceKey.class));
    }

    @Override
    public CosmosBuilder<ResourceKey> optional() {
        this.builder.optional();
        return this;
    }

}
