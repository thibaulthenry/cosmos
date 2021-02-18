package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import org.spongepowered.api.world.weather.WeatherType;
import org.spongepowered.api.world.weather.WeatherTypes;

import java.util.Collections;
import java.util.List;

@Singleton
public class Sun extends AbstractWeatherModifyCommand {

    @Override
    protected List<String> additionalAliases() {
        return Collections.singletonList("clear");
    }

    @Override
    protected WeatherType newWeather() {
        return WeatherTypes.CLEAR.get();
    }

}
