package cosmos.executors.parameters.builders.backup;

import cosmos.Cosmos;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.builders.CosmosBuilder;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

public class BackupWorld implements CosmosBuilder<ResourceKey> {

    private final Parameter.Value.Builder<ResourceKey> builder;

    public BackupWorld() {
        final ValueParameter<ResourceKey> value = VariableValueParameters.dynamicChoicesBuilder(ResourceKey.class) // todo resourkey
                .setChoicesAndResults(() -> Cosmos.getServices().backup().getBackupWorldMap())
                .build();
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
