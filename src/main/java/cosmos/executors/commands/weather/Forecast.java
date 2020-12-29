package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorldProperties;
import org.spongepowered.api.world.weather.Weather;

@Singleton
public class Forecast extends AbstractWeatherCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final Weather weather) throws CommandException {
        this.serviceProvider.message()
                .getMessage(src, "success.weather.get")
                .replace("world", properties)
                .replace("value", weather)
                .replace("running", properties.getRunningWeatherDuration().getExpectedDuration(Sponge.getServer()).getSeconds()) // TODO Buggy
                .replace("remaining", properties.getRemainingWeatherDuration().getExpectedDuration(Sponge.getServer()).getSeconds())
                .successColor()
                .sendTo(src);
    }
}
