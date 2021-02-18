package cosmos.executors.parameters.scoreboard;

import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.Team;

public class TeamAll implements CosmosBuilder<Team> {

    private final Parameter.Value.Builder<Team> builder;

    public TeamAll() {
        final ValueParameter<Team> value = new TeamFilter(team -> true);
        this.builder = Parameter.builder(Team.class, value);
        this.builder.key(CosmosKeys.TEAM);
    }

    @Override
    public Parameter.Value<Team> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<Team> key(final Parameter.Key<Team> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<Team> key(final String key) {
        return this.key(Parameter.key(key, Team.class));
    }

    @Override
    public CosmosBuilder<Team> optional() {
        this.builder.optional();
        return this;
    }

}
