package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Operands;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Singleton
public class Operation extends AbstractMultiTargetCommand {

    @Inject
    public Operation(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get(),
                injector.getInstance(ObjectiveAll.class).builder().build(),
                CosmosParameters.SCOREBOARD_OPERANDS,
                injector.getInstance(Targets.class).get(), // todo source
                injector.getInstance(ObjectiveAll.class).builder().setKey("source-objective").build() // todo
        );
    }

    private Collection<Component> atomicMutation(final Objective targetObjective, final Component target, final Operands operand, final Objective sourceObjective, final Component source) {
        if (this.serviceProvider.validation().doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
            return Collections.singleton(Component.empty()); // todo return Collections.singleton(Outputs.TOO_LONG_PLAYER_NAME.asText(target));
        }

        final Score targetScore = targetObjective.getOrCreateScore(target);
        final int targetScoreValue = targetScore.getScore();

        if (this.serviceProvider.validation().doesOverflowMaxLength(source, Units.PLAYER_NAME_MAX_LENGTH)) {
            return Collections.singleton(Component.empty()); // todo return Collections.singleton(Outputs.TOO_LONG_PLAYER_NAME.asText(source));
        }

        final Score sourceScore = sourceObjective.getOrCreateScore(source);

        try {
            final int mutationResult = operate(targetScoreValue, operand, sourceScore.getScore());
            // todo addSuccess();
            targetScore.setScore(mutationResult);
        } catch (final ArithmeticException ignored) {
            return Collections.singleton(Component.empty()); // todo  Collections.singleton(Outputs.OVERFLOWING_OPERATION.asText(operand, target));
        }

        final Collection<Component> operationOutputs = new ArrayList<>();

        operationOutputs.add(Component.empty()/* todo Outputs.OPERATE_SCORE.asText(mutationResult, "target", target, targetObjective)*/);

        if (operand == Operands.SWAPS) {
            // todo addSuccess();
            sourceScore.setScore(targetScoreValue);
            operationOutputs.add(Component.empty()/* todo Outputs.OPERATE_SCORE.asText(sourceScore.getScore(), "source", source, sourceObjective)*/);
        }

        return operationOutputs;
    }

    private int operate(int targetScoreValue, Operands operand, int sourceScoreValue) throws ArithmeticException {
        switch (operand) {
            case PLUS:
                return Math.addExact(targetScoreValue, sourceScoreValue);
            case MINUS:
                return Math.subtractExact(targetScoreValue, sourceScoreValue);
            case TIMES:
                return Math.multiplyExact(targetScoreValue, sourceScoreValue);
            case DIVIDE:
                if (sourceScoreValue == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return targetScoreValue / sourceScoreValue;
            case MODULUS:
                if (sourceScoreValue == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return targetScoreValue % sourceScoreValue;
            case MIN:
                return Math.min(targetScoreValue, sourceScoreValue);
            case MAX:
                return Math.max(targetScoreValue, sourceScoreValue);
            default:
                return sourceScoreValue;
        }
    }


    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective targetObjective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));
        final Objective sourceObjective = context.getOne(Parameter.key("source-objective", Objective.class))
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        final Collection<Component> sources = super.extractTargets(context, worldKey, super.getScoreboard(worldKey)); // todo add source targets

        if (targets.size() > 1 && sources.size() > 1) {
            throw new CommandException(Component.empty()); // todo throw Outputs.INVALID_CROSS_OPERATION.asException();
        }

        final Operands operand = context.getOne(CosmosParameters.SCOREBOARD_OPERANDS)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_OPERAND_CHOICE.asSupplier());

        final Collection<Component> contents = targets
                .stream()
                .map(target -> sources
                        .stream()
                        .map(source -> atomicMutation(targetObjective, target, operand, sourceObjective, source))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), operand, worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }

}
