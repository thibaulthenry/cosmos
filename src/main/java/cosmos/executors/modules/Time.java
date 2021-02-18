package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.time.*;

@Singleton
class Time extends AbstractModule {

    @Inject
    Time(final Injector injector) {
        super(
                CosmosParameters.WORLD_ALL.get().optional().build(),
                injector.getInstance(Calendar.class),
                injector.getInstance(Dawn.class),
                injector.getInstance(Dusk.class),
                injector.getInstance(IgnorePlayersSleeping.class),
                injector.getInstance(Midday.class),
                injector.getInstance(Midnight.class),
                injector.getInstance(RealTime.class),
                injector.getInstance(Set.class),
                injector.getInstance(Tomorrow.class)
        );
    }

}
