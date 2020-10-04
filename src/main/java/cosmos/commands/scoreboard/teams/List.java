package cosmos.commands.scoreboard.teams;

import cosmos.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class List extends AbstractScoreboardCommand {

    public List() {
        super(
                GenericArguments.optional(
                        Arguments.limitCompleteElement(
                                ScoreboardArguments.teamChoices(ArgKeys.TEAM, ArgKeys.WORLD)
                        )
                )
        );
    }

    private static PaginationList getTeamMembers(String worldName, Team team) {
        Set<Text> contents = team.getMembers();
        Text title = Outputs.SHOW_TEAM_MEMBERS.asText(contents.size(), team, worldName);
        return generatePaginationOutput(title, contents);
    }

    private PaginationList getRegisteredTeamsText(String worldName) {
        Collection<Text> contents = getScoreboard().getTeams()
                .stream()
                .map(team -> Outputs.SHOW_TRACKED_TEAM.asText(team, team.getDisplayName(), team.getMembers().size()))
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_ALL_TRACKED_TEAMS.asText(contents.size(), worldName);

        return generatePaginationOutput(title, contents);
    }


    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        if (getScoreboard().getTeams().isEmpty()) {
            throw Outputs.MISSING_TEAM.asException(worldName);
        }

        Optional<Team> optionalTeam = args.getOne(ArgKeys.TEAM.t);

        PaginationList paginationList = optionalTeam
                .map(team -> getTeamMembers(worldName, team))
                .orElseGet(() -> getRegisteredTeamsText(worldName));

        sendPaginatedOutput(src, paginationList, false);
    }
}
