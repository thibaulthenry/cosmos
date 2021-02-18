package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.perworld.Group;
import cosmos.executors.commands.perworld.Toggle;

@Singleton
class PerWorld extends AbstractModule {

    @Inject
    PerWorld(final Injector injector) {
        super(
                CosmosParameters.PER_WORLD_LISTENER.get().key(CosmosKeys.PER_WORLD_FEATURE).build(),
                injector.getInstance(Group.class),
                injector.getInstance(Toggle.class)
        );
    }

}
