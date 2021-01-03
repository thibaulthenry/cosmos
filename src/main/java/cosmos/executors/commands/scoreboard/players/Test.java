package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.ObjectiveAll;
import cosmos.executors.parameters.impl.scoreboard.Targets;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Test extends AbstractMultiTargetCommand {

    @Inject
    public Test(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get(),
                injector.getInstance(ObjectiveAll.class).builder().build(),
                Parameter.integerNumber().setKey(CosmosKeys.MIN).build(), // todo extremum
                Parameter.integerNumber().setKey(CosmosKeys.MAX).optional().build() // todo extremum
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final int min = context.getOne(CosmosKeys.MIN)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.NOT_A_NUMBER.asSupplier("Min value"));
        final int max = context.getOne(CosmosKeys.MAX)
                .orElse(Integer.MAX_VALUE);

        if (min > max) {
            throw new CommandException(Component.empty()); // todo throw Outputs.INVALID_DIFFERENCE.asException("min", "max", 1);
        }

        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        final Collection<Component> contents = targets.stream()
                .map(target -> {
                    final Optional<Score> optionalScore = objective.getScore(target);

                    if (!optionalScore.isPresent()) {
                        return Component.empty(); // todo return Outputs.MISSING_TARGET_SCORE.asText(target, objective);
                    }

                    final int score = optionalScore.get().getScore();

                    if (score >= min && score <= max) {
                        // todo addSuccess();
                        return Component.empty(); // todo return Outputs.TEST_SCORE.asText(score, target, min, max);
                    }

                    return Component.empty(); // todo return Outputs.TESTING_SCORE.asText(score, target, min, max);
                })
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "testing", worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
