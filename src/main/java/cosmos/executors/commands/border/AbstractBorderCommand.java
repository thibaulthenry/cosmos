package cosmos.executors.commands.border;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.ServerWorldProperties;

abstract class AbstractBorderCommand extends AbstractCommand {

    AbstractBorderCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorldProperties properties = this.serviceProvider.world().getProperties(context);
        this.run(src, context, properties, properties.getWorldBorder());
    }

    protected abstract void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException;
}
