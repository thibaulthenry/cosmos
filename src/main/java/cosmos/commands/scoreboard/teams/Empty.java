package cosmos.commands.scoreboard.teams;

import cosmos.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;

import java.util.Collection;

public class Empty extends AbstractScoreboardCommand {

    public Empty() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.teamChoices(ArgKeys.TEAM, ArgKeys.WORLD)
                )
        );
    }

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        Team team = args.<Team>getOne(ArgKeys.TEAM.t).orElseThrow(Outputs.INVALID_TEAM_CHOICE.asSupplier());

        Collection<Text> members = team.getMembers();

        if (members.isEmpty()) {
            throw Outputs.EMPTY_TEAM.asException(team);
        }

        members.forEach(team::removeMember);

        src.sendMessage(Outputs.REMOVE_ALL_PLAYERS_FROM_TEAM.asText(members.size(), team, worldName));
    }
}
