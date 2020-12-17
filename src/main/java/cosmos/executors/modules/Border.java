package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.border.Center;
import cosmos.executors.commands.border.DamageAmount;
import cosmos.executors.commands.border.DamageThreshold;
import cosmos.executors.commands.border.DefaultSettings;
import cosmos.executors.commands.border.Information;
import cosmos.executors.commands.border.Remove;
import cosmos.executors.commands.border.Size;
import cosmos.executors.commands.border.Transpose;
import cosmos.executors.commands.border.WarningDistance;
import cosmos.executors.commands.border.WarningTime;
import cosmos.executors.parameters.CosmosKeys;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
class Border extends AbstractModule {

    @Inject
    Border(final Injector injector) {
        super(
                Parameter.worldProperties(false).setKey(CosmosKeys.WORLD).optional().build(),
                injector.getInstance(Center.class),
                injector.getInstance(DamageAmount.class),
                injector.getInstance(DamageThreshold.class),
                injector.getInstance(DefaultSettings.class),
                injector.getInstance(Information.class),
                injector.getInstance(Remove.class),
                injector.getInstance(Size.class),
                injector.getInstance(Transpose.class),
                injector.getInstance(WarningDistance.class),
                injector.getInstance(WarningTime.class)
        );
    }
}
