package cosmos.executors.modules.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.portal.modify.Cooldown;
import cosmos.executors.commands.portal.modify.Destination;
import cosmos.executors.commands.portal.modify.Trigger;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.modules.portal.modify.Particles;

public class Modify extends AbstractModule {

    @Inject
    Modify(final Injector injector) {
        super(
                false,
                injector.getInstance(Cooldown.class),
                injector.getInstance(Destination.class),
                injector.getInstance(Particles.class),
                injector.getInstance(Trigger.class)
        );
    }

}
