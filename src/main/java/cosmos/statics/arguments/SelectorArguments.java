package cosmos.statics.arguments;

import cosmos.statics.arguments.implementations.selector.EntityCommandElement;
import cosmos.statics.arguments.implementations.selector.PlayerCommandElement;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

public class SelectorArguments {

    public static CommandElement entity(Text key) {
        return new EntityCommandElement(key, false, false, null);
    }

    public static CommandElement entityOrSource(Text key) {
        return new EntityCommandElement(key, true, false, null);
    }

    public static CommandElement playerOrSource(Text key) {
        return new PlayerCommandElement(key, true);
    }
}
