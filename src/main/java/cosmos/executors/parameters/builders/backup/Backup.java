package cosmos.executors.parameters.builders.backup;

import cosmos.Cosmos;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.builders.CosmosBuilder;
import cosmos.registries.backup.BackupArchetype;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

public class Backup implements CosmosBuilder<BackupArchetype> {

    private final Parameter.Value.Builder<BackupArchetype> builder;

    public Backup() {
        this.builder = Parameter.builder(BackupArchetype.class)
                .parser(
                        VariableValueParameters.dynamicChoicesBuilder(BackupArchetype.class)
                                .setChoicesAndResults(() -> Cosmos.getServices().backup().getBackupMap())
                                .build()
                );
        this.builder.setKey(CosmosKeys.BACKUP);
    }

    @Override
    public Parameter.Value<BackupArchetype> build() {
        return this.builder.build();
    }

    public CosmosBuilder<BackupArchetype> key(final Parameter.Key<BackupArchetype> key) {
        this.builder.setKey(key);
        return this;
    }

    public CosmosBuilder<BackupArchetype> key(final String key) {
        return this.key(Parameter.key(key, BackupArchetype.class));
    }

    @Override
    public CosmosBuilder<BackupArchetype> optional() {
        this.builder.optional();
        return this;
    }

}
