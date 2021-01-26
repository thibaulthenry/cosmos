package cosmos.executors.parameters.impl.backup;

import com.google.inject.Inject;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.clientcompletion.ClientCompletionType;
import org.spongepowered.api.command.parameter.managed.clientcompletion.ClientCompletionTypes;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

import java.util.Collections;
import java.util.List;

public class BackupWorld implements CosmosBuilder<ResourceKey> {

    private final Parameter.Value.Builder<ResourceKey> builder;

    @Inject
    private BackupWorld(final ServiceProvider serviceProvider) {
        final ValueParameter<ResourceKey> value = VariableValueParameters.dynamicChoicesBuilder(ResourceKey.class) // todo resourkey
                .setChoicesAndResults(() -> serviceProvider.backup().getBackupWorldMap())
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
