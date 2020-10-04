package cosmos.commands.scoreboard;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderScoreboard;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.text.Text;

import java.util.Collection;

public abstract class AbstractMultiTargetCommand extends AbstractScoreboardCommand {

    protected AbstractMultiTargetCommand(CommandElement... commandElements) {
        super(commandElements);
        enableSuccessCount();
    }

    protected static Collection<Text> extractTargets(CommandContext args, ArgKeys argName, String worldName, Scoreboard scoreboard) throws CommandException {
        boolean isTargetsFilled = args.hasAny(ArgKeys.IS_FILLED.t);

        Collection<Text> inputTargets = args.getAll(argName.t);

        Collection<Text> targets = !isTargetsFilled || Validator.hasAsterisk(inputTargets) ?
                FinderScoreboard.getTrackedPlayers(scoreboard) :
                inputTargets;

        if (targets.isEmpty()) {
            throw isTargetsFilled ?
                    Outputs.MISSING_MATCHING_TARGET.asException(worldName) :
                    Outputs.MISSING_TRACKED_PLAYERS.asException(worldName);
        }

        return targets;
    }

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        Collection<Text> targets = extractTargets(args, ArgKeys.TARGETS, worldName, scoreboard);
        runWithTargets(src, args, worldName, targets);
    }

    protected abstract void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException;
}
