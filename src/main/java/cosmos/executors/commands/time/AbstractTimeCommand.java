package cosmos.executors.commands.time;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

public abstract class AbstractTimeCommand extends AbstractCommand {

    protected AbstractTimeCommand(Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorldProperties properties = this.serviceProvider.world().getPropertiesOrSource(context);
        this.run(src, context, properties, properties.dayTime());
    }

    protected abstract void run(Audience src, CommandContext context, ServerWorldProperties properties, MinecraftDayTime time) throws CommandException;

}
