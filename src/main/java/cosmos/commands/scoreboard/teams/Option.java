package cosmos.commands.scoreboard.teams;

import cosmos.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.FormattingCodes;
import cosmos.constants.Outputs;
import cosmos.constants.TeamOptions;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.CollisionRule;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

// TODO SpongePowered/SpongeCommon Issue #3198

public class Option extends AbstractScoreboardCommand {

    public Option() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.teamChoices(ArgKeys.TEAM, ArgKeys.WORLD)
                ),
                ScoreboardArguments.teamOptionChoices(ArgKeys.TEAM_OPTION),
                ScoreboardArguments.teamOptionValueChoices(ArgKeys.VALUE, ArgKeys.TEAM_OPTION)
        );
    }

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        Team team = args.<Team>getOne(ArgKeys.TEAM.t).orElseThrow(Outputs.INVALID_TEAM_CHOICE.asSupplier());

        TeamOptions teamOption = args.<TeamOptions>getOne(ArgKeys.TEAM_OPTION.t)
                .orElseThrow(Outputs.INVALID_TEAM_OPTION_CHOICE.asSupplier());

        Object teamOptionValue = args.getOne(ArgKeys.VALUE.t)
                .orElseThrow(Outputs.INVALID_TEAM_OPTION_VALUE_CHOICE.asSupplier(teamOption.getKey()));

        switch (teamOption) {
            case COLLISION_RULE:
                team.setCollisionRule((CollisionRule) teamOptionValue);
                break;
            case COLOR:
                TextColor color = (TextColor) teamOptionValue;
                team.setColor(color);
                FormattingCodes.getFromElement(color).ifPresent(formattingCode -> {
                    team.setPrefix(Text.of("§", formattingCode.getCode()));
                    team.setPrefix(Text.of("§r"));
                });
                break;
            case DEATH_MESSAGE_VISIBILITY:
                team.setDeathMessageVisibility((Visibility) teamOptionValue);
                break;
            case FRIENDLYFIRE:
                team.setAllowFriendlyFire((Boolean) teamOptionValue);
                break;
            case NAMETAG_VISIBILITY:
                team.setNameTagVisibility((Visibility) teamOptionValue);
                break;
            case SEE_FRIENDLY_INVISIBLES:
                team.setCanSeeFriendlyInvisibles((Boolean) teamOptionValue);
                break;
        }

        src.sendMessage(Outputs.SET_TEAM_OPTION.asText(teamOption.getKey(), team, teamOptionValue, worldName));
    }
}
