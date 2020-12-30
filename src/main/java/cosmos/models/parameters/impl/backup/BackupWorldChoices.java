package cosmos.models.parameters.impl.backup;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

@Singleton
public class BackupWorldChoices implements CosmosBuilder<ResourceKey> {

    private final ValueParameter<ResourceKey> value;

    @Inject
    private BackupWorldChoices(final ServiceProvider serviceProvider) {
        this.value = VariableValueParameters.dynamicChoicesBuilder(ResourceKey.class)
                .setChoicesAndResults(() -> serviceProvider.backup().getBackupWorldMap())
                .build();
    }

    public Parameter.Value.Builder<ResourceKey> builder() {
        return Parameter.builder(ResourceKey.class, this.value).setKey(CosmosKeys.WORLD_KEY);
    }
}
