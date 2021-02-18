package cosmos.executors.parameters.backup;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

public class BackupWorld implements CosmosBuilder<ResourceKey> {

    // TODO https://github.com/SpongePowered/Sponge/issues/3272
    private static final ValueParameter<ResourceKey> BACKUP_WORLD = VariableValueParameters.dynamicChoicesBuilder(ResourceKey.class)
            .choicesAndResults(() -> Cosmos.services().backup().backupWorldMap())
            .build();

    private final Parameter.Value.Builder<ResourceKey> builder;

    public BackupWorld() {
        this.builder = Parameter.builder(ResourceKey.class, BackupWorld.BACKUP_WORLD);
        this.builder.key(CosmosKeys.WORLD);
    }

    @Override
    public Parameter.Value<ResourceKey> build() {
        return this.builder.build();
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
