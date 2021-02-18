package cosmos.executors.modules.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.portal.modify.sound.Ambient;
import cosmos.executors.commands.portal.modify.sound.Delay;
import cosmos.executors.commands.portal.modify.sound.Travel;
import cosmos.executors.commands.portal.modify.sound.Trigger;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Sound extends AbstractModule {

    @Inject
    Sound(final Injector injector) {
        super(
                CosmosParameters.PORTAL_ALL.get().build(),
                false,
                injector.getInstance(Ambient.class),
                injector.getInstance(Delay.class),
                injector.getInstance(Travel.class),
                injector.getInstance(Trigger.class)
        );
    }

}
