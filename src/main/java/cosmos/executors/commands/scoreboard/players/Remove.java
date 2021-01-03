package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Units;
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
import java.util.stream.Collectors;

@Singleton
public class Remove extends AbstractMultiTargetCommand {

    @Inject
    public Remove(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get(),
                injector.getInstance(ObjectiveAll.class).builder().build(),
                Parameter.integerNumber().setKey(CosmosKeys.AMOUNT).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName)

        final int amount = context.getOne(CosmosKeys.AMOUNT)
                .filter(value -> value > 0)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo Outputs.GREATER_THAN.asSupplier(0)

        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    if (this.serviceProvider.validation().doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                        return Component.empty(); // todo return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
                    }

                    try {
                        final Score score = objective.getOrCreateScore(target);
                        final int result = Math.subtractExact(score.getScore(), amount);
                        score.setScore(result);

                        //addSuccess();
                        return Component.empty(); // todo return Outputs.REMOVE_TO_SCORE.asText(amount, target, objective, result);
                    } catch (ArithmeticException ignored) {
                        return Component.empty(); // todo return Outputs.OVERFLOWING_OPERATION.asText("Subtraction", target);
                    }
                })
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "subtraction(s)", worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
