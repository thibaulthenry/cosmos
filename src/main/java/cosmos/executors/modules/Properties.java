package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.properties.*;

@Singleton
class Properties extends AbstractModule {

    @Inject
    Properties(final Injector injector) {
        super(
                CosmosParameters.WORLD_ALL.get().optional().build(),
                injector.getInstance(Difficulty.class),
                injector.getInstance(EnableCommandBlocks.class),
                injector.getInstance(EnableFeatures.class),
                injector.getInstance(GameMode.class),
                injector.getInstance(GameRule.class),
                injector.getInstance(Hardcore.class),
                injector.getInstance(KeepSpawnLoaded.class),
                injector.getInstance(LoadOnStartup.class),
                injector.getInstance(Pvp.class),
                injector.getInstance(SpawnPosition.class),
                injector.getInstance(ViewDistance.class)
        );
    }

}
