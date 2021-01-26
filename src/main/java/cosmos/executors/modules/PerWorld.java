package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.modules.perworld.Feature;
import cosmos.executors.modules.perworld.Group;

@Singleton
class PerWorld extends AbstractModule {

    @Inject
    PerWorld(final Injector injector) {
        super(
                injector.getInstance(Feature.class),
                injector.getInstance(Group.class)
        );
    }

}
