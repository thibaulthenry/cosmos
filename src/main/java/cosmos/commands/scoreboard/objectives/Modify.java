package cosmos.commands.scoreboard.objectives;

import cosmos.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.ModifyCommands;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.text.Text;

public class Modify extends AbstractScoreboardCommand {

    public Modify() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                ),
                ScoreboardArguments.modifyCommandChoices(ArgKeys.MODIFY_COMMAND),
                ScoreboardArguments.modifyCommandValue(ArgKeys.VALUE, ArgKeys.MODIFY_COMMAND)
        );
    }

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        Objective objective = args.<Objective>getOne(ArgKeys.OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        ModifyCommands modifyCommand = args.<ModifyCommands>getOne(ArgKeys.MODIFY_COMMAND.t)
                .orElseThrow(Outputs.INVALID_MODIFY_COMMAND_CHOICE.asSupplier());

        Object modifyCommandValue = args.getOne(ArgKeys.VALUE.t)
                .orElseThrow(Outputs.INVALID_MODIFY_COMMAND_VALUE.asSupplier(modifyCommand.getKey()));

        switch (modifyCommand) {
            case DISPLAY_NAME:
                objective.setDisplayName((Text) modifyCommandValue);
                break;
            case RENDER_TYPE:
                objective.setDisplayMode((ObjectiveDisplayMode) modifyCommandValue);
                break;
        }

        src.sendMessage(Outputs.MODIFY_OBJECTIVE.asText(modifyCommandValue, modifyCommand.getKey(), objective, worldName));
    }
}
