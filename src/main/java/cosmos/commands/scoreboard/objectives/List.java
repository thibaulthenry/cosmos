package cosmos.commands.scoreboard.objectives;

import cosmos.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.constants.Outputs;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Set;
import java.util.stream.Collectors;

public class List extends AbstractScoreboardCommand {

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        if (scoreboard.getObjectives().isEmpty()) {
            throw Outputs.MISSING_OBJECTIVE.asException(worldName);
        }

        Set<Objective> objectives = scoreboard.getObjectives();

        Text title = Outputs.SHOW_ALL_TRACKED_OBJECTIVES.asText(objectives.size(), worldName);

        Iterable<Text> contents = objectives
                .stream()
                .map(objective -> Outputs.SHOW_TRACKED_OBJECTIVE.asText(objective, objective.getDisplayName(), objective.getCriterion()))
                .collect(Collectors.toList());

        sendPaginatedOutput(src, title, contents, false);
    }
}
