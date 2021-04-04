package cosmos.modules;

import cosmos.commands.weather.Forecast;
import cosmos.commands.weather.Rain;
import cosmos.commands.weather.Sun;
import cosmos.commands.weather.Thunder;
import cosmos.constants.ArgKeys;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldPropertiesArguments;

class Weather extends AbstractModule {

    Weather() {
        super("Weather module commands");

        addArgument(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.allChoices(ArgKeys.WORLD, true)
                )
        );
        addChild(new Forecast());
        addChild(new Rain());
        addChild(new Sun());
        addChild(new Thunder());
    }
}
