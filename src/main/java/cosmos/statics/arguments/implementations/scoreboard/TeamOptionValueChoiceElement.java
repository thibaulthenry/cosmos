package cosmos.statics.arguments.implementations.scoreboard;

import cosmos.constants.Outputs;
import cosmos.constants.TeamOptions;
import cosmos.statics.arguments.implementations.EnhancedCommandElement;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.StartsWithPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class TeamOptionValueChoiceElement extends EnhancedCommandElement {

    private final Text teamOptionKey;
    private Supplier<Collection<String>> keySupplier;
    private Function<String, ?> valueSupplier;

    public TeamOptionValueChoiceElement(@Nullable Text key, @Nonnull Text teamOptionKey) {
        super(key);
        this.teamOptionKey = teamOptionKey;
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        context.<TeamOptions>getOne(teamOptionKey)
                .ifPresent(teamOption -> {
                    setKeySupplier(teamOption);
                    setValueSupplier(teamOption);
                });

        super.parse(source, args, context);
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (keySupplier == null || valueSupplier == null) {
            return Collections.emptyList();
        }

        Object value = valueSupplier.apply(args.next());

        if (value == null) {
            throw args.createError(Outputs.INVALID_CHOICE.asText(keySupplier.get().toString()));
        }

        return value;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if (keySupplier == null) {
            return Collections.emptyList();
        }

        String prefix = args.nextIfPresent().orElse("");
        return keySupplier.get()
                .stream()
                .filter(new StartsWithPredicate(prefix)).collect(Collectors.toList());
    }

    private void setKeySupplier(TeamOptions teamOption) {
        if (!teamOption.getCatalogClass().isPresent()) {
            keySupplier = () -> Arrays.asList("true", "false");
            return;
        }

        keySupplier = () -> Sponge.getRegistry().getAllOf(teamOption.getCatalogClass().get())
                .stream()
                .map(CatalogType::getName)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private void setValueSupplier(TeamOptions teamOption) {
        if (!teamOption.getCatalogClass().isPresent()) {
            valueSupplier = Boolean::parseBoolean;
            return;
        }

        valueSupplier = (key) -> Sponge.getRegistry().getAllOf(teamOption.getCatalogClass().get())
                .stream()
                .filter(value -> key.equals(value.getName()))
                .findFirst()
                .orElse(null);
    }
}
