package cosmos.executors.modules.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.portal.modify.sound.Ambiance;
import cosmos.executors.commands.portal.modify.sound.Delay;
import cosmos.executors.commands.portal.modify.sound.Travel;
import cosmos.executors.commands.portal.modify.sound.Trigger;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.parameters.CosmosParameters;

public class Sound extends AbstractModule {

    @Inject
    Sound(final Injector injector) {
        super(
                CosmosParameters.Builder.PORTAL_ALL.get().build(),
                false,
                injector.getInstance(Ambiance.class),
                injector.getInstance(Delay.class),
                injector.getInstance(Travel.class),
                injector.getInstance(Trigger.class)
        );
    }

}
