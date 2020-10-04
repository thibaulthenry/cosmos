package cosmos.statics.arguments;

import cosmos.constants.ArgKeys;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorldPropertiesArguments {

    public static CommandElement allChoices(ArgKeys argName, boolean includeDisabled) {
        return ChoicesArguments.choices(
                argName,
                () -> toMap(FinderWorldProperties.getAllWorldProperties(includeDisabled)),
                false);
    }

    public static CommandElement loadedChoices(ArgKeys argName) {
        return ChoicesArguments.choices(
                argName,
                () -> toMap(FinderWorldProperties.getLoadedWorldProperties()),
                false);
    }

    public static CommandElement unloadedChoices(ArgKeys argName, boolean includeDisabled) {
        return ChoicesArguments.choices(
                argName,
                () -> toMap(FinderWorldProperties.getUnloadedWorldProperties(includeDisabled)),
                false);
    }

    private static Map<String, WorldProperties> toMap(Collection<WorldProperties> collection) {
        return collection
                .stream()
                .distinct()
                .collect(Collectors.toMap(WorldProperties::getWorldName, Function.identity()));
    }
}
