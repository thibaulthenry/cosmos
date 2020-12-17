package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.properties.Difficulty;
import cosmos.executors.commands.properties.EnableCommandBlocks;
import cosmos.executors.commands.properties.EnableStructures;
import cosmos.executors.commands.properties.GameMode;
import cosmos.executors.commands.properties.GenerateSpawnOnLoad;
import cosmos.executors.commands.properties.GeneratorType;
import cosmos.executors.commands.properties.Hardcore;
import cosmos.executors.commands.properties.KeepSpawnLoaded;
import cosmos.executors.commands.properties.LoadOnStartup;
import cosmos.executors.commands.properties.Pvp;
import cosmos.executors.commands.properties.SpawnPosition;
import cosmos.executors.commands.properties.ViewDistance;
import cosmos.executors.parameters.CosmosKeys;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
class Properties extends AbstractModule {

    @Inject
    Properties(final Injector injector) {
        super(
                Parameter.worldProperties(false).setKey(CosmosKeys.WORLD).optional().build(),
                injector.getInstance(Difficulty.class),
                injector.getInstance(EnableCommandBlocks.class),
                injector.getInstance(EnableStructures.class),
                injector.getInstance(GameMode.class),
                injector.getInstance(GenerateSpawnOnLoad.class),
                injector.getInstance(GeneratorType.class),
                injector.getInstance(Hardcore.class),
                injector.getInstance(KeepSpawnLoaded.class),
                injector.getInstance(LoadOnStartup.class),
                injector.getInstance(Pvp.class),
                injector.getInstance(SpawnPosition.class),
                injector.getInstance(ViewDistance.class)
        );
    }
}
