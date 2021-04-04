package cosmos.modules;

import cosmos.constants.ArgKeys;
import cosmos.modules.scoreboard.Objectives;
import cosmos.modules.scoreboard.Players;
import cosmos.modules.scoreboard.Teams;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldPropertiesArguments;

class Scoreboard extends AbstractModule {

    Scoreboard() {
        super("Scoreboard module commands", true);

        addArgument(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.allChoices(ArgKeys.WORLD, true)
                )
        );
        addChild(new Objectives());
        addChild(new Players());
        addChild(new Teams());
    }
}
