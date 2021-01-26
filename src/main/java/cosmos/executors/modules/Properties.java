package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.properties.Difficulty;
import cosmos.executors.commands.properties.EnableCommandBlocks;
import cosmos.executors.commands.properties.EnableFeatures;
import cosmos.executors.commands.properties.GameMode;
import cosmos.executors.commands.properties.GameRule;
import cosmos.executors.commands.properties.Hardcore;
import cosmos.executors.commands.properties.KeepSpawnLoaded;
import cosmos.executors.commands.properties.LoadOnStartup;
import cosmos.executors.commands.properties.Pvp;
import cosmos.executors.commands.properties.SpawnPosition;
import cosmos.executors.commands.properties.ViewDistance;
import cosmos.executors.parameters.impl.world.WorldAll;

@Singleton
class Properties extends AbstractModule {

    @Inject
    Properties(final Injector injector) {
        super(
                injector.getInstance(WorldAll.class).optional().build(),
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
