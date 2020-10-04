package cosmos.commands.scoreboard.objectives;

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
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Optional;

public class SetDisplay extends AbstractScoreboardCommand {

    public SetDisplay() {
        super(
                ScoreboardArguments.displaySlotChoices(ArgKeys.DISPLAY_SLOT),
                GenericArguments.optional(
                        Arguments.limitCompleteElement(
                                ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                        )
                )
        );
    }

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        DisplaySlot displaySlot = args.<DisplaySlot>getOne(ArgKeys.DISPLAY_SLOT.t)
                .orElseThrow(Outputs.INVALID_DISPLAY_SLOT_CHOICE.asSupplier());

        Optional<Objective> optionalObjective = args.getOne(ArgKeys.OBJECTIVE.t);

        if (optionalObjective.isPresent()) {
            Objective objective = optionalObjective.get();
            scoreboard.updateDisplaySlot(objective, displaySlot);
            src.sendMessage(Outputs.SET_DISPLAY_SLOT.asText(objective, displaySlot, worldName));
        } else {
            scoreboard.getObjective(displaySlot).orElseThrow(Outputs.MISSING_DISPLAY_SLOT.asSupplier(worldName));
            scoreboard.clearSlot(displaySlot);
            src.sendMessage(Outputs.CLEAR_DISPLAY_SLOT.asText(displaySlot, worldName));
        }
    }
}
