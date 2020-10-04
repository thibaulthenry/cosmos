package cosmos.modules;

import cosmos.commands.time.Calendar;
import cosmos.commands.time.Dawn;
import cosmos.commands.time.Dusk;
import cosmos.commands.time.IgnorePlayersSleeping;
import cosmos.commands.time.Midday;
import cosmos.commands.time.Midnight;
import cosmos.commands.time.RealTime;
import cosmos.commands.time.Set;
import cosmos.commands.time.Tomorrow;
import cosmos.constants.ArgKeys;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldPropertiesArguments;

class Time extends AbstractModule {

    Time() {
        super("Time module commands");


        addArgument(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.allChoices(ArgKeys.WORLD, true)
                )
        );
        addChild(new Calendar());
        addChild(new Dawn());
        addChild(new IgnorePlayersSleeping());
        addChild(new Midday());
        addChild(new Midnight());
        addChild(new Dusk());
        addChild(new RealTime());
        addChild(new Set());
        addChild(new Tomorrow());
    }
}
