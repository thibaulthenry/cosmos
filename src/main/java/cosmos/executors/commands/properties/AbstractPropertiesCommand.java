package cosmos.executors.commands.properties;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

abstract class AbstractPropertiesCommand extends AbstractCommand {

    AbstractPropertiesCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        this.run(src, context, super.serviceProvider.world().getPropertiesOrSource(context));
    }

    protected abstract void run(Audience src, CommandContext context, ServerWorldProperties properties) throws CommandException;

}
