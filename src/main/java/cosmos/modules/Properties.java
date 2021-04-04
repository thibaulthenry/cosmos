package cosmos.modules;

import cosmos.commands.properties.AllowCommandBlocks;
import cosmos.commands.properties.Difficulty;
import cosmos.commands.properties.EnableStructures;
import cosmos.commands.properties.GameMode;
import cosmos.commands.properties.GenerateSpawnOnLoad;
import cosmos.commands.properties.GeneratorType;
import cosmos.commands.properties.Hardcore;
import cosmos.commands.properties.KeepSpawnLoaded;
import cosmos.commands.properties.LoadOnStartup;
import cosmos.commands.properties.Pvp;
import cosmos.commands.properties.Rules;
import cosmos.commands.properties.Seed;
import cosmos.commands.properties.SpawnPosition;
import cosmos.constants.ArgKeys;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldPropertiesArguments;

class Properties extends AbstractModule {

    Properties() {
        super("Properties module commands");

        addArgument(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.allChoices(ArgKeys.WORLD, true)
                )
        );
        addChild(new AllowCommandBlocks());
        addChild(new Difficulty());
        addChild(new EnableStructures());
        addChild(new GameMode());
        addChild(new GenerateSpawnOnLoad());
        addChild(new GeneratorType());
        addChild(new Hardcore());
        addChild(new KeepSpawnLoaded());
        addChild(new LoadOnStartup());
        addChild(new Pvp());
        addChild(new Rules());
        addChild(new Seed());
        addChild(new SpawnPosition());
    }
}
