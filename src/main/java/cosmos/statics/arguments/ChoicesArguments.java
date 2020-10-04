package cosmos.statics.arguments;

import cosmos.constants.ArgKeys;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class ChoicesArguments {

    static CommandElement collectionChoices(ArgKeys argName, Supplier<Collection<String>> collectionSupplier, boolean choicesInUsage) {
        return GenericArguments.choices(
                argName.t,
                collectionSupplier,
                key -> toIdentityMap(collectionSupplier.get()).get(key),
                choicesInUsage);
    }

    static <T> CommandElement choices(ArgKeys argName, Supplier<Map<String, T>> mapSupplier, boolean choicesInUsage) {
        return GenericArguments.choices(
                argName.t,
                () -> mapSupplier.get().keySet(),
                key -> mapSupplier.get().get(key),
                choicesInUsage);
    }

    private static <T> Map<T, T> toIdentityMap(Collection<T> collection) {
        return collection
                .stream()
                .distinct()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
    }
}
