package cosmos.statics.arguments.implementations;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public abstract class EnhancedCommandElement extends CommandElement {

    protected EnhancedCommandElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public abstract Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException;

}
