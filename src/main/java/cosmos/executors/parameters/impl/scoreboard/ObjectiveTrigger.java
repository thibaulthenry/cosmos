package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.criteria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;

public class ObjectiveTrigger implements CosmosBuilder<Objective> {

    private final Parameter.Value.Builder<Objective> builder;

    @Inject
    private ObjectiveTrigger(final ServiceProvider serviceProvider) {
        final ValueParameter<Objective> value = new ObjectiveFilter(
                objective -> Criteria.TRIGGER.get().equals(objective.getCriterion()),
                serviceProvider
        );
        this.builder = Parameter.builder(Objective.class, value);
        this.builder.setKey(CosmosKeys.OBJECTIVE);
    }

    @Override
    public Parameter.Value<Objective> build() {
        return this.builder.build();
    }

    public CosmosBuilder<Objective> key(final Parameter.Key<Objective> key) {
        this.builder.setKey(key);
        return this;
    }

    public CosmosBuilder<Objective> key(final String key) {
        return this.key(Parameter.key(key, Objective.class));
    }

    @Override
    public CosmosBuilder<Objective> optional() {
        this.builder.optional();
        return this;
    }

}
