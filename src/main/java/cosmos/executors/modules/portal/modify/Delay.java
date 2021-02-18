package cosmos.executors.modules.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.portal.modify.delay.Duration;
import cosmos.executors.commands.portal.modify.delay.Format;
import cosmos.executors.commands.portal.modify.delay.GradientColors;
import cosmos.executors.commands.portal.modify.delay.Show;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Delay extends AbstractModule {

    @Inject
    Delay(final Injector injector) {
        super(
                CosmosParameters.PORTAL_ALL.get().build(),
                false,
                injector.getInstance(Duration.class),
                injector.getInstance(Format.class),
                injector.getInstance(GradientColors.class),
                injector.getInstance(Show.class)
        );
    }

}
