package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.objective.Objective;

@Singleton
public class ObjectiveAll implements CosmosBuilder<Objective> {

    private final ValueParameter<org.spongepowered.api.scoreboard.objective.Objective> value;

    @Inject
    private ObjectiveAll(final ServiceProvider serviceProvider) {
        this.value = new ObjectiveFilter(
                objective -> true,
                serviceProvider
        );
    }

    @Override
    public Parameter.Value.Builder<Objective> builder() {
        return Parameter.builder(Objective.class, this.value).setKey(CosmosKeys.OBJECTIVE);
    }
}
