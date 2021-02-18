package cosmos.executors.parameters.world;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class WorldDistinct implements CosmosBuilder<ResourceKey> {

    private final Parameter.Value.Builder<ResourceKey> builder;

    public WorldDistinct() {
        final ValueParameter<ResourceKey> value = new WorldFilter(
                src -> Cosmos.services().message()
                        .getMessage(src, "error.invalid.world.distinct")
                        .replace("param", CosmosKeys.WORLD),
                (worldKey, context) -> !context.all(CosmosKeys.WORLD).contains(worldKey)
        );
        this.builder = Parameter.builder(ResourceKey.class, value);
        this.builder.key(CosmosKeys.WORLD);
    }

    @Override
    public Parameter.Value<ResourceKey> build() {
        return this.builder.build();
    }

    public CosmosBuilder<ResourceKey> consumeAllRemaining() {
        this.builder.consumeAllRemaining();
        return this;
    }

    @Override
    public CosmosBuilder<ResourceKey> key(final Parameter.Key<ResourceKey> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<ResourceKey> key(final String key) {
        return this.key(Parameter.key(key, ResourceKey.class));
    }

    @Override
    public CosmosBuilder<ResourceKey> optional() {
        this.builder.optional();
        return this;
    }

}
