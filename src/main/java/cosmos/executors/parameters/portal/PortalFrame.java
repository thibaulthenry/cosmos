package cosmos.executors.parameters.portal;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import cosmos.registries.portal.CosmosFramePortal;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalFrame implements CosmosBuilder<CosmosFramePortal> {

    private final Parameter.Value.Builder<CosmosFramePortal> builder;

    public PortalFrame() {
        final ValueParameter<CosmosFramePortal> value = new PortalFilter<>(
                CosmosFramePortal.class,
                src -> Cosmos.services().message()
                        .getMessage(src, "error.invalid.value")
                        .replace("param", CosmosKeys.PORTAL_FRAME_COSMOS),
                portal -> true);
        this.builder = Parameter.builder(CosmosFramePortal.class, value);
        this.builder.key(CosmosKeys.PORTAL_FRAME_COSMOS);
    }

    @Override
    public Parameter.Value<CosmosFramePortal> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<CosmosFramePortal> key(final Parameter.Key<CosmosFramePortal> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<CosmosFramePortal> key(final String key) {
        return this.key(Parameter.key(key, CosmosFramePortal.class));
    }

    @Override
    public CosmosBuilder<CosmosFramePortal> optional() {
        this.builder.optional();
        return this;
    }

}
