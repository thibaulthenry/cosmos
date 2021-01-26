package cosmos.executors.commands.scoreboard;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Collection;

public abstract class AbstractMultiTargetCommand extends AbstractScoreboardCommand {

    private final boolean returnSource;

    protected AbstractMultiTargetCommand(final boolean returnSource, final Parameter... parameters) {
        super(parameters);
        this.returnSource = returnSource;
    }

    protected AbstractMultiTargetCommand(final Parameter... parameters) {
        this(false, parameters);
    }

    @Override
    protected int initializeSuccessCount() {
        return 0;
    }

    @Override
    protected final void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        this.run(src, context, worldKey, super.serviceProvider.perWorld().scoreboards().getTargets(context, worldKey, this.returnSource));
    }

    protected abstract void run(Audience src, CommandContext context, ResourceKey worldKey, Collection<Component> targets) throws CommandException;

}
