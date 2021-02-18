package cosmos.executors.parameters.portal;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import cosmos.registries.portal.CosmosPortal;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalAll implements CosmosBuilder<CosmosPortal> {

    private final Parameter.Value.Builder<CosmosPortal> builder;

    public PortalAll() {
        final ValueParameter<CosmosPortal> value = new PortalFilter<>(
                CosmosPortal.class,
                src -> Cosmos.services().message()
                        .getMessage(src, "error.invalid.value")
                        .replace("param", CosmosKeys.PORTAL_COSMOS),
                portal -> true);
        this.builder = Parameter.builder(CosmosPortal.class, value);
        this.builder.key(CosmosKeys.PORTAL_COSMOS);
    }

    @Override
    public Parameter.Value<CosmosPortal> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<CosmosPortal> key(final Parameter.Key<CosmosPortal> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<CosmosPortal> key(final String key) {
        return this.key(Parameter.key(key, CosmosPortal.class));
    }

    @Override
    public CosmosBuilder<CosmosPortal> optional() {
        this.builder.optional();
        return this;
    }

}
