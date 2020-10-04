package cosmos.commands.scoreboard.players;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.Units;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.stream.Collectors;

public class Remove extends AbstractMultiTargetCommand {

    public Remove() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                ),
                GenericArguments.integer(ArgKeys.AMOUNT.t)
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Objective objective = args.<Objective>getOne(ArgKeys.OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        int amount = args.<Integer>getOne(ArgKeys.AMOUNT.t).filter(value -> value > 0)
                .orElseThrow(Outputs.GREATER_THAN.asSupplier(0));

        Collection<Text> contents = targets.stream().map(target -> {
            if (Validator.doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
            }

            try {
                Score score = objective.getOrCreateScore(target);
                int result = Math.subtractExact(score.getScore(), amount);
                score.setScore(result);

                addSuccess();
                return Outputs.REMOVE_TO_SCORE.asText(amount, target, objective, result);
            } catch (ArithmeticException ignored) {
                return Outputs.OVERFLOWING_OPERATION.asText("Subtraction", target);
            }
        }).collect(Collectors.toList());

        Text title = Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "subtraction(s)", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }

}
