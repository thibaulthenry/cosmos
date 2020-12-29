package cosmos.executors.commands.weather;

import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerWorldProperties;
import org.spongepowered.api.world.weather.Weather;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

public abstract class AbstractWeatherChangeCommand extends AbstractWeatherCommand {

    protected AbstractWeatherChangeCommand() {
        super(CosmosParameters.DURATION_WITH_TIME_UNIT_OPTIONAL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final Weather weather) throws CommandException {
        final Optional<Long> optionalInput = context.getOne(CosmosKeys.DURATION);
        final boolean hasInput = optionalInput.isPresent();
        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        final Weather newWeather = this.getNewWeather();

        if (hasInput) {
            final Ticks ticks = Ticks.ofWallClockTime(Sponge.getServer(), optionalInput.get(), unit);
            properties.setWeather(newWeather, ticks);
        } else {
            properties.setWeather(newWeather, properties.getRemainingWeatherDuration());
        }

        this.serviceProvider.worldProperties().save(properties);

        this.serviceProvider.message()
                .getMessage(src, "success.weather.set")
                .replace("world", properties)
                .replace("value", newWeather)
                .replace("duration", optionalInput.orElse(0L))
                .replace("unit", unit)
                .condition("for", hasInput)
                .condition("duration", hasInput)
                .condition("unit", hasInput)
                .successColor()
                .sendTo(src);
    }

    protected abstract Weather getNewWeather();
}
