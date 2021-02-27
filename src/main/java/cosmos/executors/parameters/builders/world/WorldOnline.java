package cosmos.executors.parameters.builders.world;

import cosmos.Cosmos;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.builders.CosmosBuilder;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class WorldOnline implements CosmosBuilder<ResourceKey> {

    private final Parameter.Value.Builder<ResourceKey> builder;

    public WorldOnline() {
        final ValueParameter<ResourceKey> value = new WorldFilter(
                src -> Cosmos.getServices().message()
                        .getMessage(src, "error.invalid.world.online")
                        .replace("parameter", CosmosKeys.WORLD),
                worldKey -> Cosmos.getServices().world().isOnline(worldKey)
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
