package cosmos.statics.arguments.implementations;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class ExtremumElement extends EnhancedCommandElement {

    private final boolean positive;

    public ExtremumElement(@Nullable Text key, boolean positive) {
        super(key);
        this.positive = positive;
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String input = args.next();

        if ("*".equals(input)) {
            return positive ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
            throw args.createError(Text.of(String.format("Expected an integer, but input '%s' was not", input)));
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Collections.emptyList();
    }
}
