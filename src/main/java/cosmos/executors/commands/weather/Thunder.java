package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.Weathers;

@Singleton
public class Thunder extends AbstractWeatherChangeCommand {

    @Override
    protected Weather getNewWeather() {
        return Weathers.THUNDER.get();
    }

}
