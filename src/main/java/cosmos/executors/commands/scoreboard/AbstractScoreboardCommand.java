package cosmos.executors.commands.scoreboard;

import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.listener.impl.perworld.ScoreboardsListener;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;

public abstract class AbstractScoreboardCommand extends AbstractCommand {

    protected AbstractScoreboardCommand(final Parameter... parameters) {
        super(parameters);
    }

    protected Scoreboard getScoreboard(final ResourceKey worldKey) {
        return this.serviceProvider.perWorld().scoreboards().getOrCreateScoreboard(worldKey);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        if (!this.serviceProvider.listener().isRegistered(ScoreboardsListener.class)) {
            throw new CommandException(Component.empty()); // todo throw Outputs.PER_WORLD_SCOREBOARDS_DISABLED.asException();
        }


        final ResourceKey worldKey = this.serviceProvider.world().getKeyOrSource(context);
        this.run(src, context, worldKey, this.getScoreboard(worldKey));
    }

    protected abstract void run(Audience src, CommandContext context, ResourceKey worldKey, Scoreboard scoreboard) throws CommandException;

}
