package cosmos.executors.commands.properties;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerWorldProperties;

abstract class AbstractPropertiesCommand extends AbstractCommand {

    AbstractPropertiesCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorldProperties properties = this.serviceProvider.worldProperties().get(context);
        this.run(src, context, properties);
    }

    protected abstract void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException;
}
