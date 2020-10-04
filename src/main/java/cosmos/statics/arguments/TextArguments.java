package cosmos.statics.arguments;

import cosmos.constants.ArgKeys;
import cosmos.statics.arguments.implementations.EnhancedCommandElement;
import cosmos.statics.arguments.implementations.PlayerNameElement;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.serializer.TextSerializers;

public class TextArguments {

    public static EnhancedCommandElement allTexts(ArgKeys argName) {
        return Arguments.firstParsing(
                argName,
                GenericArguments.text(argName.t, TextSerializers.JSON, false),
                GenericArguments.text(argName.t, TextSerializers.FORMATTING_CODE, false),
                GenericArguments.text(argName.t, TextSerializers.LEGACY_FORMATTING_CODE, false)
        );
    }

    static CommandElement playerNames(ArgKeys argName, ArgKeys worldKey, boolean returnSource) {
        return new PlayerNameElement(argName.t, worldKey.t, returnSource);
    }
}
