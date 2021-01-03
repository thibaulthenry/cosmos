package cosmos.executors.commands.scoreboard;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.Collections;

public abstract class AbstractMultiTargetCommand extends AbstractScoreboardCommand {

    protected AbstractMultiTargetCommand(Parameter... parameters) {
        super(parameters);
        //enableSuccessCount(); todo
    }

    protected Collection<Component> extractTargets(final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
// todo
//        Collection<Component> inputTargets = args.getAll();
//
//        Collection<Text> targets = !isTargetsFilled || Validator.hasAsterisk(inputTargets) ?
//                FinderScoreboard.getTrackedPlayers(scoreboard) :
//                inputTargets;
//
//        if (targets.isEmpty()) {
//            throw isTargetsFilled ?
//                    Outputs.MISSING_MATCHING_TARGET.asException(worldKey) :
//                    Outputs.MISSING_TRACKED_PLAYERS.asException(worldKey);
//        }
//
//        return targets;
        return Collections.emptyList();
    }

    @Override
    protected final void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        this.run(src, context, worldKey, this.extractTargets(context, worldKey, scoreboard));
    }

    protected abstract void run(Audience src, CommandContext context, ResourceKey worldKey, Collection<Component> targets) throws CommandException;
}
