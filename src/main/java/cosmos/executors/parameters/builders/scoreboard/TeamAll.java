package cosmos.executors.parameters.builders.scoreboard;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.builders.CosmosBuilder;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.Team;

public class TeamAll implements CosmosBuilder<Team> {

    private final Parameter.Value.Builder<Team> builder;

    public TeamAll() {
        final ValueParameter<Team> value = new TeamFilter(team -> true);
        this.builder = Parameter.builder(Team.class, value);
        this.builder.setKey(CosmosKeys.TEAM);
    }

    @Override
    public Parameter.Value<Team> build() {
        return this.builder.build();
    }

    public CosmosBuilder<Team> key(final Parameter.Key<Team> key) {
        this.builder.setKey(key);
        return this;
    }

    public CosmosBuilder<Team> key(final String key) {
        return this.key(Parameter.key(key, Team.class));
    }

    @Override
    public CosmosBuilder<Team> optional() {
        this.builder.optional();
        return this;
    }

}
