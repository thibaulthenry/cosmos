package cosmos.executors.commands.border;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

abstract class AbstractBorderCommand extends AbstractCommand {

    AbstractBorderCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected void run(final Audience audience, final CommandContext context) throws CommandException {
        final WorldProperties properties = this.serviceProvider.worldProperties().get(context);
        this.run(audience, context, properties, properties.getWorldBorder());
    }

    protected abstract void run(final Audience audience, final CommandContext context, final WorldProperties properties, final WorldBorder border) throws CommandException;
}
