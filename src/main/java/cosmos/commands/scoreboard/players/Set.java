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
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.stream.Collectors;

public class Set extends AbstractMultiTargetCommand {

    public Set() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                ),
                GenericArguments.integer(ArgKeys.SCORE.t)
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Objective objective = args.<Objective>getOne(ArgKeys.OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        int score = args.<Integer>getOne(ArgKeys.SCORE.t).orElseThrow(Outputs.INVALID_VALUE.asSupplier());

        Collection<Text> contents = targets.stream().map(target -> {
            if (Validator.doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
            }

            objective.getOrCreateScore(target).setScore(score);

            addSuccess();
            return Outputs.SET_SCORE.asText(score, target, objective);
        }).collect(Collectors.toList());

        Text title = Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "mutation(s)", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
