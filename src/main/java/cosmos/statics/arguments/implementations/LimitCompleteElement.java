package cosmos.statics.arguments.implementations;

import cosmos.constants.Outputs;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class LimitCompleteElement extends EnhancedCommandElement {

    private final CommandElement element;
    private final int limit;

    public LimitCompleteElement(CommandElement element, int limit) {
        super(null);
        this.element = element;
        this.limit = limit;
    }

    @Nullable
    public Text getKey() {
        return element.getKey();
    }

    @Nullable
    public String getUntranslatedKey() {
        return element.getUntranslatedKey();
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        element.parse(source, args, context);
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) {
        return null;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        List<String> choices = element.complete(src, args, context);

        if (choices.isEmpty() || choices.size() <= limit) {
            return choices;
        }

        String[] arguments = args.getRaw().split(" ");
        String lastArg = arguments[arguments.length - 1];
        String firstChoice = choices.get(0);

        if (firstChoice.length() > lastArg.length()) {
            char commonFirstChar = firstChoice.substring(lastArg.length()).charAt(0);

            boolean filledWithCommonPrefix = choices.stream()
                    .filter(choice -> choice.length() > lastArg.length())
                    .map(choice -> choice.substring(lastArg.length()))
                    .allMatch(choice -> choice.charAt(0) == commonFirstChar);

            if (filledWithCommonPrefix) {
                return choices;
            }
        }

        src.sendMessage(Outputs.TOO_MANY_CHOICES.asText(limit));
        return choices.subList(0, limit);
    }

    @Override
    public Text getUsage(CommandSource src) {
        return element.getUsage(src);
    }
}
