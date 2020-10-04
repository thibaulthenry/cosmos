package cosmos.commands.scoreboard.teams;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class Leave extends AbstractMultiTargetCommand {

    public Leave() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD, true)
                )
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Collection<Text> contents = targets
                .stream()
                .map(target -> {
                    Optional<Team> optionalMemberTeam = getScoreboard().getMemberTeam(target);

                    if (optionalMemberTeam.isPresent()) {
                        Team memberTeam = optionalMemberTeam.get();
                        memberTeam.removeMember(target);
                        addSuccess();
                        return Outputs.LEAVE_TEAM.asText(target, memberTeam);
                    }

                    return Outputs.MISSING_TARGET_TEAM.asText(target);
                })
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_TEAM_OPERATIONS.asText(contents.size(), "unsubscription(s)", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
