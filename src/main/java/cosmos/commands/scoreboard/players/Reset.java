package cosmos.commands.scoreboard.players;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class Reset extends AbstractMultiTargetCommand {

    public Reset() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                GenericArguments.optional(
                        Arguments.limitCompleteElement(
                                ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                        )
                )
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Optional<Objective> optionalObjective = args.getOne(ArgKeys.OBJECTIVE.t);

        Collection<Objective> objectives = optionalObjective
                .map(Collections::singleton)
                .orElse(getScoreboard().getObjectives());

        Collection<Text> contents = targets.stream().map(target -> objectives
                .stream()
                .filter(objective -> objective.hasScore(target))
                .map(objective -> {
                    objective.removeScore(target);
                    addSuccess();
                    return Outputs.RESET_SCORE.asText(target, objective);
                })
                .collect(Collectors.toList())
        ).flatMap(Collection::stream).collect(Collectors.toList());

        Text title = Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "erasure(s)", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
