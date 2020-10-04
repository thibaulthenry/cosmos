package cosmos.modules;

import cosmos.commands.border.Center;
import cosmos.commands.border.DamageAmount;
import cosmos.commands.border.DamageThreshold;
import cosmos.commands.border.Information;
import cosmos.commands.border.Operate;
import cosmos.commands.border.Remove;
import cosmos.commands.border.Size;
import cosmos.commands.border.Transpose;
import cosmos.commands.border.WarningDistance;
import cosmos.commands.border.WarningTime;
import cosmos.constants.ArgKeys;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldArguments;

class Border extends AbstractModule {

    Border() {
        super("Border module commands");

        addArgument(
                Arguments.limitCompleteElement(
                        WorldArguments.loadedChoices(ArgKeys.LOADED_WORLD)
                )
        );
        addChild(new Center());
        addChild(new DamageAmount());
        addChild(new DamageThreshold());
        addChild(new Information());
        addChild(new Operate());
        addChild(new Remove());
        addChild(new Size());
        addChild(new Transpose());
        addChild(new WarningDistance());
        addChild(new WarningTime());
    }
}
