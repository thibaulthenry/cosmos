package cosmos.executors.parameters.perworld;

import com.google.common.base.Functions;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import cosmos.registries.listener.Listener;
import cosmos.registries.listener.impl.perworld.AbstractPerWorldListener;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Feature implements CosmosBuilder<Listener> {

    private static final Supplier<Map<String, ? extends Listener>> FEATURE_MAP_SUPPLIER = () -> {
        if (Feature.FEATURE_MAP != null) {
            return Feature.FEATURE_MAP;
        }

        Feature.FEATURE_MAP = Cosmos.services().registry().listener()
                .stream()
                .filter(listener -> listener instanceof AbstractPerWorldListener)
                .collect(
                        Collectors.toMap(
                                listener -> Cosmos.services().listener().format(listener.getClass()),
                                Functions.identity()
                        )
                );

        return Feature.FEATURE_MAP;
    };

    private static final ValueParameter<Listener> PER_WORLD_LISTENERS = VariableValueParameters.dynamicChoicesBuilder(Listener.class)
            .choicesAndResults(Feature.FEATURE_MAP_SUPPLIER)
            .showInUsage(false)
            .build();

    private static Map<String, Listener> FEATURE_MAP;

    private final Parameter.Value.Builder<Listener> builder;

    public Feature() {
        this.builder = Parameter.builder(Listener.class, Feature.PER_WORLD_LISTENERS);
        this.builder.key(CosmosKeys.PER_WORLD_FEATURE);
    }

    @Override
    public Parameter.Value<Listener> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<Listener> key(final Parameter.Key<Listener> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<Listener> key(final String key) {
        return this.key(Parameter.key(key, Listener.class));
    }

    @Override
    public CosmosBuilder<Listener> optional() {
        this.builder.optional();
        return this;
    }

}
