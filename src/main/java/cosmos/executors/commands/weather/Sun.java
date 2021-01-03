package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.Weathers;

import java.util.Collections;
import java.util.List;

@Singleton
public class Sun extends AbstractWeatherChangeCommand {

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("clear");
    }

    @Override
    protected Weather getNewWeather() {
        return Weathers.CLEAR.get();
    }

}
