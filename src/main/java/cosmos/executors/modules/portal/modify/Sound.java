package cosmos.executors.modules.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.portal.modify.sound.Ambiance;
import cosmos.executors.commands.portal.modify.sound.Travel;
import cosmos.executors.commands.portal.modify.sound.Trigger;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.portal.PortalAll;
import cosmos.executors.parameters.impl.portal.PortalFrame;

public class Sound extends AbstractModule {

    @Inject
    Sound(final Injector injector) {
        super(
                new PortalAll().key(CosmosKeys.PORTAL_COSMOS).build(),
                false,
                injector.getInstance(Ambiance.class),
                injector.getInstance(Travel.class),
                injector.getInstance(Trigger.class)
        );
    }

}
