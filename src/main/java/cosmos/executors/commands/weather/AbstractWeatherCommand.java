package cosmos.executors.commands.weather;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.api.world.weather.Weather;

public abstract class AbstractWeatherCommand extends AbstractCommand {

    protected AbstractWeatherCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorldProperties properties = this.serviceProvider.world().getPropertiesOrSource(context);
        this.run(src, context, properties, properties.weather());
    }

    protected abstract void run(Audience src, CommandContext context, ServerWorldProperties properties, Weather weather) throws CommandException;

}
