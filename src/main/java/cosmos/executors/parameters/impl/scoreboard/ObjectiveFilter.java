package cosmos.executors.parameters.impl.scoreboard;

import cosmos.services.ServiceProvider;
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
    private final ServiceProvider serviceProvider;

    ObjectiveFilter(final Predicate<Objective> filter, final ServiceProvider serviceProvider) {
        this.filter = filter;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return this.serviceProvider.world().findKeyOrSource(context)
                .map(worldKey -> this.serviceProvider.perWorld().scoreboards().getObjectives(worldKey))
                .orElse(Collections.emptySet())
                .stream()
                .filter(this.filter)
                .map(Objective::getName)
                .filter(name -> name.startsWith(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends Objective> getValue(final Parameter.Key<? super Objective> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final String input = reader.peekString();

        return this.serviceProvider.world().findKeyOrSource(context)
                .map(worldKey -> this.serviceProvider.perWorld().scoreboards().getOrCreateScoreboard(worldKey))
                .flatMap(scoreboard -> scoreboard.getObjective(input));
    }
}
