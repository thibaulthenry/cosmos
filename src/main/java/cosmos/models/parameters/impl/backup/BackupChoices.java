package cosmos.models.parameters.impl.backup;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.models.backup.BackupArchetype;
import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

@Singleton
public class BackupChoices implements CosmosBuilder<BackupArchetype> {

    private final ValueParameter<BackupArchetype> value;

    @Inject
    private BackupChoices(final ServiceProvider serviceProvider) {
        this.value = VariableValueParameters.dynamicChoicesBuilder(BackupArchetype.class)
                .setChoicesAndResults(() -> serviceProvider.backup().getBackupMap())
                .build();
    }

    public Parameter.Value.Builder<BackupArchetype> builder() {
        return Parameter.builder(BackupArchetype.class, this.value).setKey(CosmosKeys.BACKUP);
    }
}
