package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import org.spongepowered.api.world.weather.WeatherType;
import org.spongepowered.api.world.weather.WeatherTypes;

@Singleton
public class Rain extends AbstractWeatherModifyCommand {

    @Override
    protected WeatherType newWeather() {
        return WeatherTypes.RAIN.get();
    }

}