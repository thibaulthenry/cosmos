package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.border.*;

@Singleton
class Border extends AbstractModule {

    @Inject
    Border(final Injector injector) {
        super(
                CosmosParameters.WORLD_ALL.get().optional().build(),
                injector.getInstance(Center.class),
                injector.getInstance(DamageAmount.class),
                injector.getInstance(DamageThreshold.class),
                injector.getInstance(DefaultSettings.class),
                injector.getInstance(Information.class),
                injector.getInstance(Operate.class),
                injector.getInstance(Remove.class),
                injector.getInstance(Size.class),
                injector.getInstance(Transpose.class),
                injector.getInstance(WarningDistance.class),
                injector.getInstance(WarningTime.class)
        );
    }

}
