package cosmos.executors.commands.scoreboard;

import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.listener.impl.perworld.ScoreboardsListener;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;

public abstract class AbstractScoreboardCommand extends AbstractCommand {

    protected AbstractScoreboardCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        if (!super.serviceProvider.listener().isRegistered(ScoreboardsListener.class)) {
            throw super.serviceProvider.message().getError(src, "error.per-world.scoreboards.disabled");
        }

        final ResourceKey worldKey = super.serviceProvider.world().getKeyOrSource(context);
        this.run(src, context, worldKey, super.serviceProvider.perWorld().scoreboards().getOrCreateScoreboard(worldKey));
    }

    protected abstract void run(Audience src, CommandContext context, ResourceKey worldKey, Scoreboard scoreboard) throws CommandException;

}
