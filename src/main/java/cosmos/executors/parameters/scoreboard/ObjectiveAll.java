package cosmos.executors.parameters.scoreboard;

import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.objective.Objective;

public class ObjectiveAll implements CosmosBuilder<Objective> {

    private final Parameter.Value.Builder<Objective> builder;

    public ObjectiveAll() {
        final ValueParameter<Objective> value = new ObjectiveFilter(objective -> true);
        this.builder = Parameter.builder(Objective.class, value);
        this.builder.key(CosmosKeys.OBJECTIVE);
    }

    @Override
    public Parameter.Value<Objective> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<Objective> key(final Parameter.Key<Objective> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<Objective> key(final String key) {
        return this.key(Parameter.key(key, Objective.class));
    }

    @Override
    public CosmosBuilder<Objective> optional() {
        this.builder.optional();
        return this;
    }

}
