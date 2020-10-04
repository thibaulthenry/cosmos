package cosmos.commands.scoreboard.teams;

import cosmos.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.Units;
import cosmos.statics.arguments.TextArguments;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class Add extends AbstractScoreboardCommand {

    public Add() {
        super(
                GenericArguments.string(ArgKeys.TEAM_NAME.t),
                GenericArguments.optional(
                        TextArguments.allTexts(ArgKeys.DISPLAY_NAME)
                )
        );
    }


    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        String teamName = args.<String>getOne(ArgKeys.TEAM_NAME.t).orElseThrow(Outputs.INVALID_TEAM.asSupplier());

        if (Validator.doesOverflowMaxLength(teamName, Units.NAME_MAX_LENGTH)) {
            throw Outputs.TOO_LONG_TEAM_NAME.asException(teamName);
        }

        if (scoreboard.getTeam(teamName).isPresent()) {
            throw Outputs.EXISTING_TEAM.asException(teamName);
        }

        Team.Builder teamBuilder = Team.builder()
                .name(teamName);

        Optional<Text> optionalDisplayName = args.getOne(ArgKeys.DISPLAY_NAME.t);

        if (optionalDisplayName.isPresent()) {
            Text displayName = optionalDisplayName.get();

            if (Validator.doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH)) {
                throw Outputs.TOO_LONG_TEAM_DISPLAY_NAME.asException(displayName);
            }

            teamBuilder.displayName(displayName);
        }

        Team team = teamBuilder.build();

        scoreboard.registerTeam(team);

        src.sendMessage(Outputs.ADD_TEAM.asText(team, worldName));
    }
}
