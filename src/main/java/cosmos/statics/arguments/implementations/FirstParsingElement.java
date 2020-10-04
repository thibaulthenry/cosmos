package cosmos.statics.arguments.implementations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"NullableProblems", "StaticPseudoFunctionalStyleMethod"})
public class FirstParsingElement extends EnhancedCommandElement {

    private final List<CommandElement> elements;
    private final boolean optionalUsage;

    public FirstParsingElement(@Nonnull Text key, boolean optionalUsage, CommandElement... elements) {
        super(key);
        this.optionalUsage = optionalUsage;
        this.elements = Arrays.asList(elements);
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        ArgumentParseException lastException = null;
        for (CommandElement element : elements) {
            CommandArgs.Snapshot startState = args.getSnapshot();
            CommandContext.Snapshot contextSnapshot = context.createSnapshot();
            try {
                element.parse(source, args, context);
                return;
            } catch (ArgumentParseException ex) {
                lastException = ex;
                args.applySnapshot(startState);
                context.applySnapshot(contextSnapshot);
            }
        }
        if (lastException != null) {
            throw lastException;
        }
    }

    @Override
    public Object parseValue(CommandSource source, CommandArgs args) {
        return null;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return ImmutableList.copyOf(Iterables.concat(Iterables.transform(elements,
                input -> {
                    if (input == null) {
                        return ImmutableList.of();
                    }

                    CommandArgs.Snapshot snapshot = args.getSnapshot();
                    List<String> ret = input.complete(src, args, context);
                    args.applySnapshot(snapshot);
                    return ret;
                })));
    }

    @Override
    public Text getUsage(CommandSource src) {
        return optionalUsage ? Text.of("[", super.getUsage(src), "]") : super.getUsage(src);
    }
}
