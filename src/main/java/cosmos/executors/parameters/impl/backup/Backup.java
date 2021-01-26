package cosmos.executors.parameters.impl.backup;

import com.google.inject.Inject;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

public class Backup implements CosmosBuilder<BackupArchetype> {

    private final Parameter.Value.Builder<BackupArchetype> builder;

    @Inject
    private Backup(final ServiceProvider serviceProvider) {
        this.builder = Parameter.builder(BackupArchetype.class)
                .parser(
                        VariableValueParameters.dynamicChoicesBuilder(BackupArchetype.class)
                                .setChoicesAndResults(() -> serviceProvider.backup().getBackupMap())
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
