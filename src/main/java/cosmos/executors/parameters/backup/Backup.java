package cosmos.executors.parameters.backup;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import cosmos.registries.backup.BackupArchetype;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

public class Backup implements CosmosBuilder<BackupArchetype> {

    private static final ValueParameter<BackupArchetype> BACKUP_ARCHETYPE = VariableValueParameters.dynamicChoicesBuilder(BackupArchetype.class)
            .choicesAndResults(() -> Cosmos.services().backup().backupMap())
            .showInUsage(false)
            .build();

    private final Parameter.Value.Builder<BackupArchetype> builder;

    public Backup() {
        this.builder = Parameter.builder(BackupArchetype.class).addParser(Backup.BACKUP_ARCHETYPE);
        this.builder.key(CosmosKeys.BACKUP);
    }

    @Override
    public Parameter.Value<BackupArchetype> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<BackupArchetype> key(final Parameter.Key<BackupArchetype> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<BackupArchetype> key(final String key) {
        return this.key(Parameter.key(key, BackupArchetype.class));
    }

    @Override
    public CosmosBuilder<BackupArchetype> optional() {
        this.builder.optional();
        return this;
    }

}
