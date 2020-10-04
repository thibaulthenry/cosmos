package cosmos.statics.arguments.implementations.selector;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.selector.Selector;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class ExactSelectorCommandElement extends ExactMatchingCommandElement {

    protected ExactSelectorCommandElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String arg = args.peek();
        if (arg.startsWith("@")) { // Possibly a selector
            try {
                return Selector.parse(args.next()).resolve(source);
            } catch (IllegalArgumentException ex) {
                throw args.createError(Text.of(ex.getMessage()));
            }
        }
        return super.parseValue(source, args);
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        CommandArgs.Snapshot state = args.getSnapshot();
        Optional<String> nextArg = args.nextIfPresent();
        args.applySnapshot(state);

        List<String> choices = nextArg.map(Selector::complete).orElseGet(ImmutableList::of);

        if (choices.isEmpty()) {
            choices = super.complete(src, args, context);
        }
        return choices;
    }
}
