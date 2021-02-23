package cosmos.executors.parameters.impl.portal;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.registries.portal.CosmosFramePortal;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalFrame implements CosmosBuilder<CosmosFramePortal> {

    private final Parameter.Value.Builder<CosmosFramePortal> builder;

    public PortalFrame() {
        final ValueParameter<CosmosFramePortal> value = new PortalFilter<>(CosmosFramePortal.class, portal -> true);
        this.builder = Parameter.builder(CosmosFramePortal.class, value);
        this.builder.setKey(CosmosKeys.PORTAL_FRAME_COSMOS);
    }

    @Override
    public Parameter.Value<CosmosFramePortal> build() {
        return this.builder.build();
    }

    public CosmosBuilder<CosmosFramePortal> key(final Parameter.Key<CosmosFramePortal> key) {
        this.builder.setKey(key);
        return this;
    }

    public CosmosBuilder<CosmosFramePortal> key(final String key) {
        return this.key(Parameter.key(key, CosmosFramePortal.class));
    }

    @Override
    public CosmosBuilder<CosmosFramePortal> optional() {
        this.builder.optional();
        return this;
    }

}
