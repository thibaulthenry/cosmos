package cosmos.executors.modules.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.portal.modify.Destination;
import cosmos.executors.commands.portal.modify.Nausea;
import cosmos.executors.commands.portal.modify.Trigger;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.modules.portal.modify.Delay;
import cosmos.executors.modules.portal.modify.Particles;
import cosmos.executors.modules.portal.modify.Sound;

@Singleton
public class Modify extends AbstractModule {

    @Inject
    Modify(final Injector injector) {
        super(
                false,
                injector.getInstance(Delay.class),
                injector.getInstance(Destination.class),
                injector.getInstance(Nausea.class),
                injector.getInstance(Particles.class),
                injector.getInstance(Sound.class),
                injector.getInstance(Trigger.class)
        );
    }

}
