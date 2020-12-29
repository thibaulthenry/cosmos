package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.time.Calendar;
import cosmos.executors.commands.time.Dawn;
import cosmos.executors.commands.time.Dusk;
import cosmos.executors.commands.time.IgnorePlayersSleeping;
import cosmos.executors.commands.time.Midday;
import cosmos.executors.commands.time.Midnight;
import cosmos.executors.commands.time.RealTime;
import cosmos.executors.commands.time.Set;
import cosmos.executors.commands.time.Tomorrow;
import cosmos.models.parameters.CosmosKeys;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
class Time extends AbstractModule {

    @Inject
    public Time(final Injector injector) {
        super(
                Parameter.worldProperties(false).setKey(CosmosKeys.WORLD).optional().build(),
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
