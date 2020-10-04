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

public class Remove extends AbstractScoreboardCommand {

    public Remove() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.teamChoices(ArgKeys.TEAM, ArgKeys.WORLD)
                )
        );
    }

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        Team team = args.<Team>getOne(ArgKeys.TEAM.t).orElseThrow(Outputs.INVALID_TEAM_CHOICE.asSupplier());

        if (!team.unregister()) {
            throw Outputs.REMOVING_TEAM.asException(team, worldName);
        }

        src.sendMessage(Outputs.REMOVE_TEAM.asText(team, worldName));
    }
}
