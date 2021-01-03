package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosBuilder;
import cosmos.services.ServiceProvider;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.Team;

@Singleton
public class TeamAll implements CosmosBuilder<Team> {

    private final ValueParameter<Team> value;

    @Inject
    private TeamAll(final ServiceProvider serviceProvider) {
        this.value = new TeamFilter(
                team -> true,
                serviceProvider
        );
    }

    @Override
    public Parameter.Value.Builder<Team> builder() {
        return Parameter.builder(Team.class, this.value).setKey(CosmosKeys.TEAM);
    }
}
