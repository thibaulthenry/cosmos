package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.Operands;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
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

    public Operation() {
        super(
                CosmosParameters.TARGETS.get().build(),
                CosmosParameters.OBJECTIVE_ALL.get().build(),
                CosmosParameters.SCOREBOARD_OPERANDS.get().key(CosmosKeys.OPERAND).build(),
                CosmosParameters.TARGETS.get()
                        .entitiesKey("sources")
                        .textAmpersandKey("source-text-ampersand")
                        .textJsonKey("source-text-json")
                        .build(),
                CosmosParameters.OBJECTIVE_ALL.get().key("source-objective").build()
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

        final Score targetScore = targetObjective.findOrCreateScore(target);
        final int targetScoreValue = targetScore.score();

        if (super.serviceProvider.validation().doesOverflowMaxLength(source, Units.SCORE_HOLDER_MAX_LENGTH)) {
            return super.serviceProvider.message()
                    .getMessage(src, "error.invalid.score-holder.overflow")
                    .replace("name", source)
                    .red()
                    .asSingleton();
        }

        final Score sourceScore = sourceObjective.findOrCreateScore(source);
        final int mutationResult;

        try {
            mutationResult = operate(targetScoreValue, operand, sourceScore.score());
            super.addSuccess();
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
            super.addSuccess();
            sourceScore.setScore(targetScoreValue);
            operationOutputs.add(
                    super.serviceProvider.message()
                            .getMessage(src, "success.scoreboard.players.operation")
                            .replace("obj", sourceObjective)
                            .replace("target", source)
                            .replace("score", sourceScore.score())
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
        final Objective targetObjective = context.one(CosmosKeys.OBJECTIVE)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective")
                                .replace("param", CosmosKeys.OBJECTIVE)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final Parameter.Key<Objective> sourceObjectiveKey = Parameter.key("source-objective", Objective.class);

        final Objective sourceObjective = context.one(sourceObjectiveKey)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective")
                                .replace("param", sourceObjectiveKey)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final Collection<Component> sources = super.serviceProvider.scoreboard().sources(context, worldKey, false);

        if (targets.size() > 1 && sources.size() > 1) {
            throw super.serviceProvider.message().getError(src, "error.invalid.operation.cross");
        }

        final Operands operand = context.one(CosmosKeys.OPERAND)
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
