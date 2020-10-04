package cosmos.modules.scoreboard.players;

import cosmos.constants.ArgKeys;
import cosmos.modules.AbstractModule;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;

public class Tag extends AbstractModule {

    public Tag() {
        super("Tag module commands");

        addArgument(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                )
        );
        //addChild(new Add());
        //addChild(new List());
        //addChild(new Remove());
    }
}