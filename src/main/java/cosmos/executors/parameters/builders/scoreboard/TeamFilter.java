package cosmos.executors.parameters.builders.scoreboard;

import cosmos.Cosmos;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.Team;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class TeamFilter implements ValueParameter<Team> {

    private final Predicate<Team> filter;

    TeamFilter(final Predicate<Team> filter) {
        this.filter = filter;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return Cosmos.getServices().world().findKeyOrSource(context)
                .map(worldKey -> Cosmos.getServices().perWorld().scoreboards().getTeams(worldKey))
                .orElse(Collections.emptySet())
                .stream()
                .filter(this.filter)
                .map(Team::getName)
                .filter(name -> name.startsWith(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends Team> getValue(final Parameter.Key<? super Team> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final String input = reader.parseString();

        return Cosmos.getServices().world().findKeyOrSource(context)
                .map(worldKey -> Cosmos.getServices().perWorld().scoreboards().getOrCreateScoreboard(worldKey))
                .flatMap(scoreboard -> scoreboard.getTeam(input));
    }

}
