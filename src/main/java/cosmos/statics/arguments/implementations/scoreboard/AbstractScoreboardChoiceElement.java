package cosmos.statics.arguments.implementations.scoreboard;

import cosmos.statics.arguments.implementations.EnhancedCommandElement;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.StartsWithPredicate;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
abstract class AbstractScoreboardChoiceElement<T> extends EnhancedCommandElement {

    private final Text worldKey;
    Supplier<Collection<String>> keySupplier;
    Function<String, T> valueSupplier;

    AbstractScoreboardChoiceElement(@Nullable Text key, @Nonnull Text worldKey) {
        super(key);
        this.worldKey = worldKey;
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        setSuppliersSingleton(source, context);
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
            throw args.createError(Text.of("Argument was not a valid choice. Valid choices: ", keySupplier.get().toString()));
        }

        return value;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        setSuppliersSingleton(src, context);

        String prefix = args.nextIfPresent().orElse("");
        return keySupplier.get()
                .stream()
                .filter(new StartsWithPredicate(prefix)).collect(Collectors.toList());
    }

    private void setSuppliersSingleton(CommandSource source, CommandContext context) {
        if (keySupplier == null || valueSupplier == null) {
            FinderWorldProperties.getGivenWorldPropertiesOrElse(source, context, worldKey)
                    .ifPresent(worldProperties -> {
                        setKeySupplier(worldProperties);
                        setValueSupplier(worldProperties);
                    });
        }
    }

    abstract void setKeySupplier(WorldProperties worldProperties);

    abstract void setValueSupplier(WorldProperties worldProperties);
}
