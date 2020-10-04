package cosmos.commands.scoreboard.players;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Operands;
import cosmos.constants.Outputs;
import cosmos.constants.Units;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@SuppressWarnings("ThrowsRuntimeException")
public class Operation extends AbstractMultiTargetCommand {

    public Operation() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                ),
                Arguments.operandChoices(ArgKeys.OPERAND, true),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.SOURCES, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.SOURCE_OBJECTIVE, ArgKeys.WORLD)
                )
        );
    }

    private static int operate(int targetScoreValue, Operands operand, int sourceScoreValue) throws ArithmeticException {
        switch (operand) {
            case PLUS:
                return Math.addExact(targetScoreValue, sourceScoreValue);
            case MINUS:
                return Math.subtractExact(targetScoreValue, sourceScoreValue);
            case TIMES:
                return Math.multiplyExact(targetScoreValue, sourceScoreValue);
            case DIVIDE:
                if (sourceScoreValue == 0) throw new ArithmeticException("Cannot divide by zero");
                return targetScoreValue / sourceScoreValue;
            case MODULUS:
                if (sourceScoreValue == 0) throw new ArithmeticException("Cannot divide by zero");
                return targetScoreValue % sourceScoreValue;
            case MIN:
                return Math.min(targetScoreValue, sourceScoreValue);
            case MAX:
                return Math.max(targetScoreValue, sourceScoreValue);
            default:
                return sourceScoreValue;
        }
    }

    private Collection<Text> atomicMutation(Objective targetObjective, Text target, Operands operand, Objective sourceObjective, Text source) {
        if (Validator.doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
            return Collections.singleton(Outputs.TOO_LONG_PLAYER_NAME.asText(target));
        }

        Score targetScore = targetObjective.getOrCreateScore(target);
        int targetScoreValue = targetScore.getScore();

        if (Validator.doesOverflowMaxLength(source, Units.PLAYER_NAME_MAX_LENGTH)) {
            return Collections.singleton(Outputs.TOO_LONG_PLAYER_NAME.asText(source));
        }

        Score sourceScore = sourceObjective.getOrCreateScore(source);

        int mutationResult;

        try {
            mutationResult = operate(targetScoreValue, operand, sourceScore.getScore());
            addSuccess();
            targetScore.setScore(mutationResult);
        } catch (ArithmeticException ignored) {
            return Collections.singleton(Outputs.OVERFLOWING_OPERATION.asText(operand, target));
        }

        Collection<Text> operationOutputs = new ArrayList<>();

        operationOutputs.add(Outputs.OPERATE_SCORE.asText(mutationResult, "target", target, targetObjective));

        if (operand == Operands.SWAPS) {
            addSuccess();
            sourceScore.setScore(targetScoreValue);
            operationOutputs.add(Outputs.OPERATE_SCORE.asText(sourceScore.getScore(), "source", source, sourceObjective));
        }


        return operationOutputs;
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Objective targetObjective = args.<Objective>getOne(ArgKeys.OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));
        Objective sourceObjective = args.<Objective>getOne(ArgKeys.SOURCE_OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        Collection<Text> sources = extractTargets(args, ArgKeys.SOURCES, worldName, getScoreboard());

        if (targets.size() > 1 && sources.size() > 1) {
            throw Outputs.INVALID_CROSS_OPERATION.asException();
        }

        Operands operand = args.<Operands>getOne(ArgKeys.OPERAND.t).orElseThrow(Outputs.INVALID_OPERAND_CHOICE.asSupplier());

        Collection<Text> contents = targets
                .stream()
                .map(target -> sources
                        .stream()
                        .map(source -> atomicMutation(targetObjective, target, operand, sourceObjective, source))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), operand, worldName);

        sendPaginatedOutput(src, title, contents, true);
    }


}
