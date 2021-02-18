package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.api.world.weather.Weather;

@Singleton
public class Forecast extends AbstractWeatherCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final Weather weather) throws CommandException {
        super.serviceProvider.message()
                .getMessage(src, "success.weather.get")
                .replace("remaining", properties.weather().remainingDuration().expectedDuration(Sponge.server()).getSeconds()) // todo test
                .replace("running", properties.weather().runningDuration().expectedDuration(Sponge.server()).getSeconds()) // todo test
                .replace("value", weather.type().key(RegistryTypes.WEATHER_TYPE))
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
