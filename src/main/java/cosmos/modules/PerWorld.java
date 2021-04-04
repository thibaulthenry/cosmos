package cosmos.modules;

import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.Group;
import cosmos.commands.perworld.Information;
import cosmos.commands.perworld.Toggle;
import cosmos.constants.ArgKeys;
import cosmos.statics.arguments.Arguments;

class PerWorld extends AbstractModule {

    PerWorld() {
        super("PerWorld module commands");

        addRequiredArgument(Arguments.perWorldChoices(ArgKeys.PER_WORLD_COMMAND));
        addChild(new Bypass());
        addChild(new Group());
        addChild(new Information());
        addChild(new Toggle());
    }
}
