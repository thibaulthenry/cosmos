package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.PerWorldFeatures;
import cosmos.executors.commands.perworld.Bypass;
import cosmos.executors.commands.perworld.Group;
import cosmos.executors.commands.perworld.Toggle;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
class PerWorld extends AbstractModule {

    @Inject
    PerWorld(final Injector injector) {
        super(
                Parameter.enumValue(PerWorldFeatures.class).key(CosmosKeys.PER_WORLD_FEATURE).build(),
                injector.getInstance(Bypass.class),
                injector.getInstance(Group.class),
                injector.getInstance(Toggle.class)
        );
    }

}
