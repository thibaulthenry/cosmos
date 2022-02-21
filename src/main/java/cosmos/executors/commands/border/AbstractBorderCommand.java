package cosmos.executors.commands.border;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.border.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

abstract class AbstractBorderCommand extends AbstractCommand {

    AbstractBorderCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorldProperties properties = super.serviceProvider.world().propertiesOrSource(context);
        this.run(src, context, properties, properties.worldBorder());
    }

    protected abstract void run(Audience src, CommandContext context, ServerWorldProperties properties, WorldBorder border) throws CommandException;

}
