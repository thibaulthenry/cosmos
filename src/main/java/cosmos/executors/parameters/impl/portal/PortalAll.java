package cosmos.executors.parameters.impl.portal;

import com.google.inject.Inject;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.registries.portal.CosmosPortal;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalAll implements CosmosBuilder<CosmosPortal> {

    private final Parameter.Value.Builder<CosmosPortal> builder;

    @Inject
    private PortalAll(final ServiceProvider serviceProvider) {
        final ValueParameter<CosmosPortal> value = new PortalFilter(
                src -> serviceProvider.message()
                        .getMessage(src, "error.invalid.world.offline")
                        .replace("parameter", CosmosKeys.WORLD), // todo
                portal -> true,
                serviceProvider
        );
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
