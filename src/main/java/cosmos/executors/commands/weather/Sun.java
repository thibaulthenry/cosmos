package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.Weathers;

@Singleton
public class Sun extends AbstractWeatherChangeCommand {

    @Override
    protected Weather getNewWeather() {
        return Weathers.CLEAR.get();
    }

}
