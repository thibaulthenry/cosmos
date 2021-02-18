package cosmos.executors.parameters.portal;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import cosmos.registries.data.portal.CosmosPortalType;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalTypeCosmos implements CosmosBuilder<CosmosPortalType> {

    private final Parameter.Value.Builder<CosmosPortalType> builder;

    public PortalTypeCosmos() {
        final ValueParameter<CosmosPortalType> value = new PortalTypeFilter<>(
                CosmosPortalType.class,
                src -> Cosmos.services().message()
                        .getMessage(src, "error.invalid.value")
                        .replace("param", CosmosKeys.PORTAL_TYPE_COSMOS),
                portal -> true);
        this.builder = Parameter.builder(CosmosPortalType.class, value);
        this.builder.key(CosmosKeys.PORTAL_TYPE_COSMOS);
    }

    @Override
    public Parameter.Value<CosmosPortalType> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<CosmosPortalType> key(final Parameter.Key<CosmosPortalType> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<CosmosPortalType> key(final String key) {
        return this.key(Parameter.key(key, CosmosPortalType.class));
    }

    @Override
    public CosmosBuilder<CosmosPortalType> optional() {
        this.builder.optional();
        return this;
    }

}
