package cosmos.executors.parameters.impl.portal;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.registries.portal.CosmosPortal;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalAll implements CosmosBuilder<CosmosPortal> {

    private final Parameter.Value.Builder<CosmosPortal> builder;

    public PortalAll() {
        final ValueParameter<CosmosPortal> value = new PortalFilter<>(CosmosPortal.class, portal -> true);
        this.builder = Parameter.builder(CosmosPortal.class, value);
        this.builder.setKey(CosmosKeys.PORTAL_COSMOS);
    }

    @Override
    public Parameter.Value<CosmosPortal> build() {
        return this.builder.build();
    }

    public CosmosBuilder<CosmosPortal> key(final Parameter.Key<CosmosPortal> key) {
        this.builder.setKey(key);
        return this;
    }

    public CosmosBuilder<CosmosPortal> key(final String key) {
        return this.key(Parameter.key(key, CosmosPortal.class));
    }

    @Override
    public CosmosBuilder<CosmosPortal> optional() {
        this.builder.optional();
        return this;
    }

}
