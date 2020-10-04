package cosmos.commands.scoreboard.teams;

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
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.stream.Collectors;

public class Join extends AbstractMultiTargetCommand {

    public Join() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.teamChoices(ArgKeys.TEAM, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD, true)
                )
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Team team = args.<Team>getOne(ArgKeys.TEAM.t).orElseThrow(Outputs.INVALID_TEAM_CHOICE.asSupplier());

        Collection<Text> contents = targets
                .stream()
                .map(target -> {
                    if (Validator.doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                        return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
                    }

                    team.addMember(target);

                    addSuccess();
                    return Outputs.JOIN_TEAM.asText(target, team, worldName);
                })
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_TEAM_OPERATIONS.asText(contents.size(), "registration(s)", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
