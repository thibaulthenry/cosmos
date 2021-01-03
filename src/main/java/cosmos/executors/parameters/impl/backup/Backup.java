package cosmos.executors.parameters.impl.backup;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

@Singleton
public class Backup implements CosmosBuilder<BackupArchetype> {

    private final ValueParameter<BackupArchetype> value;

    @Inject
    private Backup(final ServiceProvider serviceProvider) {
        this.value = VariableValueParameters.dynamicChoicesBuilder(BackupArchetype.class)
                .setChoicesAndResults(() -> serviceProvider.backup().getBackupMap())
                .build();
    }

    public Parameter.Value.Builder<BackupArchetype> builder() {
        return Parameter.builder(BackupArchetype.class, this.value).setKey(CosmosKeys.BACKUP);
    }
}
