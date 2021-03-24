package cosmos.executors.parameters.scoreboard;

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
        return Cosmos.services().world().findKeyOrSource(context)
                .map(worldKey -> Cosmos.services().scoreboard().teams(worldKey))
                .orElse(Collections.emptySet())
                .stream()
                .filter(this.filter)
                .map(Team::name)
                .filter(name -> name.startsWith(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends Team> parseValue(final Parameter.Key<? super Team> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final String input = reader.parseString();

        return Cosmos.services().world().findKeyOrSource(context)
                .map(worldKey -> Cosmos.services().scoreboard().scoreboardOrCreate(worldKey))
                .flatMap(scoreboard -> scoreboard.team(input));
    }

}
