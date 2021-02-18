package cosmos.executors.commands.weather;

import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.WeatherType;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

abstract class AbstractWeatherModifyCommand extends AbstractWeatherCommand {

    AbstractWeatherModifyCommand() {
        super(CosmosParameters.DURATION_WITH_UNIT.get().optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final Weather weather) throws CommandException {
        final Optional<Long> optionalInput = context.one(CosmosKeys.DURATION);
        final boolean hasInput = optionalInput.isPresent();
        final ChronoUnit unit = context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);
        final WeatherType newWeather = this.newWeather();

        if (hasInput) {
            final Ticks ticks = super.serviceProvider.time().fromDurationUnitToTicks(src, optionalInput.get(), unit);
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

    protected abstract WeatherType newWeather();

}
