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
import java.util.stream.Collectors;

@Singleton
public class Operation extends AbstractMultiTargetCommand {

    @Inject
    public Operation(final Injector injector) {
        super(
                injector.getInstance(Targets.class).build(),
                injector.getInstance(ObjectiveAll.class).build(),
                CosmosParameters.SCOREBOARD_OPERANDS,
                injector.getInstance(Targets.class).build(), // todo source
                injector.getInstance(ObjectiveAll.class).key("source-objective").build()
        );
    }

    private Collection<Component> atomicMutation(final Audience src, final Objective targetObjective, final Component target, final Operands operand, final Objective sourceObjective, final Component source) {
        if (super.serviceProvider.validation().doesOverflowMaxLength(target, Units.SCORE_HOLDER_MAX_LENGTH)) {
            return super.serviceProvider.message()
                    .getMessage(src, "error.invalid.score-holder.overflow")
                    .replace("name", target)
                    .red()
                    .asSingleton();
        }

        final Score targetScore = targetObjective.getOrCreateScore(target);
        final int targetScoreValue = targetScore.getScore();

        if (super.serviceProvider.validation().doesOverflowMaxLength(source, Units.SCORE_HOLDER_MAX_LENGTH)) {
            return super.serviceProvider.message()
                    .getMessage(src, "error.invalid.score-holder.overflow")
                    .replace("name", source)
                    .red()
                    .asSingleton();
        }

        final Score sourceScore = sourceObjective.getOrCreateScore(source);
        final int mutationResult;

        try {
            mutationResult = operate(targetScoreValue, operand, sourceScore.getScore());
            super.success();
            targetScore.setScore(mutationResult);
        } catch (final ArithmeticException ignored) {
            return super.serviceProvider.message()
                    .getMessage(src, "error.result.overflow")
                    .replace("target", target)
                    .red()
                    .asSingleton();
        }

        final Collection<Component> operationOutputs = new ArrayList<>();

        operationOutputs.add(
                super.serviceProvider.message()
                        .getMessage(src, "success.scoreboard.players.operation")
                        .replace("obj", targetObjective)
                        .replace("target", target)
                        .replace("score", mutationResult)
                        .condition("source", false)
                        .green()
                        .asText()
        );

        if (operand == Operands.SWAPS) {
            super.success();
            sourceScore.setScore(targetScoreValue);
            operationOutputs.add(
                    super.serviceProvider.message()
                            .getMessage(src, "success.scoreboard.players.operation")
                            .replace("obj", sourceObjective)
                            .replace("target", source)
                            .replace("score", sourceScore.getScore())
                            .condition("source", true)
                            .green()
                            .asText()
            );
        }

        return operationOutputs;
    }

    private int operate(final int targetScoreValue, final Operands operand, final int sourceScoreValue) throws ArithmeticException {
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
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective")
                                .replace("param", CosmosKeys.OBJECTIVE)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final Parameter.Key<Objective> sourceObjectiveKey = Parameter.key("source-objective", Objective.class);

        final Objective sourceObjective = context.getOne(sourceObjectiveKey)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective")
                                .replace("param", sourceObjectiveKey)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final Collection<Component> sources = super.serviceProvider.perWorld().scoreboards()
                .getTargets(context, worldKey, false); // todo add source targets

        if (targets.size() > 1 && sources.size() > 1) {
            throw super.serviceProvider.message().getError(src, "error.invalid.operation.cross");
        }

        final Operands operand = context.getOne(CosmosParameters.SCOREBOARD_OPERANDS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.OPERAND));

        final Collection<Component> contents = targets
                .stream()
                .map(target -> sources
                        .stream()
                        .map(source -> atomicMutation(src, targetObjective, target, operand, sourceObjective, source))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.players.processing.header")
                .replace("number", contents.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, true);
    }

}
