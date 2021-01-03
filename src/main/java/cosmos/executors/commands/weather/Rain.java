package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import org.spongepowered.api.world.weather.WeatherType;
import org.spongepowered.api.world.weather.WeatherTypes;

@Singleton
public class Rain extends AbstractWeatherChangeCommand {

    @Override
    protected WeatherType getNewWeather() {
        return WeatherTypes.RAIN.get();
    }

}