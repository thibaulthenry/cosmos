package cosmos.commands.scoreboard.players;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class Get extends AbstractMultiTargetCommand {

    public Get() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                )
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Objective objective = args.<Objective>getOne(ArgKeys.OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        Collection<Text> contents = targets.stream().map(target -> {
            Optional<Score> optionalScore = objective.getScore(target);

            return optionalScore
                    .map(score -> {
                        addSuccess();
                        return Outputs.GET_SCORE.asText(target, objective, score.getScore());
                    })
                    .orElse(Outputs.MISSING_TARGET_SCORE.asText(target.toPlain(), objective));
        }).collect(Collectors.toList());

        Text title = Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "retrieval(s)", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
