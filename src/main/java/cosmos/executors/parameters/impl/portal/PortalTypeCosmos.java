package cosmos.executors.parameters.impl.portal;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.registries.data.portal.CosmosPortalType;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalTypeCosmos implements CosmosBuilder<CosmosPortalType> {

    private final Parameter.Value.Builder<CosmosPortalType> builder;

    public PortalTypeCosmos() {
        final ValueParameter<CosmosPortalType> value = new PortalTypeFilter<>(CosmosPortalType.class, portal -> true);
        this.builder = Parameter.builder(CosmosPortalType.class, value);
        this.builder.setKey(CosmosKeys.PORTAL_TYPE_COSMOS);
    }

    @Override
    public Parameter.Value<CosmosPortalType> build() {
        return this.builder.build();
    }

    public CosmosBuilder<CosmosPortalType> key(final Parameter.Key<CosmosPortalType> key) {
        this.builder.setKey(key);
        return this;
    }

    public CosmosBuilder<CosmosPortalType> key(final String key) {
        return this.key(Parameter.key(key, CosmosPortalType.class));
    }

    @Override
    public CosmosBuilder<CosmosPortalType> optional() {
        this.builder.optional();
        return this;
    }

}
