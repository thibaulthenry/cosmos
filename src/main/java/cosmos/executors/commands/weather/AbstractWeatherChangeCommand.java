package cosmos.executors.commands.weather;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.WeatherType;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

abstract class AbstractWeatherChangeCommand extends AbstractWeatherCommand {

    AbstractWeatherChangeCommand() {
        super(CosmosParameters.DURATION_WITH_TIME_UNIT_OPTIONAL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final Weather weather) throws CommandException {
        final Optional<Long> optionalInput = context.getOne(CosmosKeys.DURATION);
        final boolean hasInput = optionalInput.isPresent();
        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        final WeatherType newWeather = this.getNewWeather();

        if (hasInput) {
            final Ticks ticks = Ticks.ofWallClockTime(Sponge.getServer(), optionalInput.get(), unit);
            properties.setWeather(newWeather, ticks);
        } else {
            properties.setWeather(newWeather, properties.weather().remainingDuration()); // tdo test
        }

        super.serviceProvider.world().saveProperties(src, properties);

        super.serviceProvider.message()
                .getMessage(src, "success.weather.set")
                .replace("duration", optionalInput.orElse(0L))
                .replace("unit", unit)
                .replace("value", newWeather.key(RegistryTypes.WEATHER_TYPE))
                .replace("world", properties)
                .condition("duration", hasInput)
                .condition("for", hasInput)
                .condition("unit", hasInput)
                .green()
                .sendTo(src);
    }

    protected abstract WeatherType getNewWeather();

}
