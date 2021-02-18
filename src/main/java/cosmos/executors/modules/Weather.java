package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.weather.Forecast;
import cosmos.executors.commands.weather.Rain;
import cosmos.executors.commands.weather.Sun;
import cosmos.executors.commands.weather.Thunder;

@Singleton
class Weather extends AbstractModule {

    @Inject
    Weather(final Injector injector) {
        super(
                CosmosParameters.WORLD_ALL.get().optional().build(),
                injector.getInstance(Forecast.class),
                injector.getInstance(Rain.class),
                injector.getInstance(Sun.class),
                injector.getInstance(Thunder.class)
        );
    }

}
