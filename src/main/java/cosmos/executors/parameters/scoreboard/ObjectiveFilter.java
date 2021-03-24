package cosmos.executors.parameters.scoreboard;

import cosmos.Cosmos;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ObjectiveFilter implements ValueParameter<Objective> {

    private final Predicate<Objective> filter;

    ObjectiveFilter(final Predicate<Objective> filter) {
        this.filter = filter;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return Cosmos.services().world().findKeyOrSource(context)
                .map(worldKey -> Cosmos.services().scoreboard().objectives(worldKey))
                .orElse(Collections.emptySet())
                .stream()
                .filter(this.filter)
                .map(Objective::name)
                .filter(name -> name.startsWith(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends Objective> parseValue(final Parameter.Key<? super Objective> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final String input = reader.parseString();

        return Cosmos.services().world().findKeyOrSource(context)
                .map(worldKey -> Cosmos.services().scoreboard().scoreboardOrCreate(worldKey))
                .flatMap(scoreboard -> scoreboard.objective(input));
    }

}
