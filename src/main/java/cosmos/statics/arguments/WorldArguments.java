package cosmos.statics.arguments;

import cosmos.constants.ArgKeys;
import cosmos.statics.finders.FinderWorld;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorldArguments {

    public static CommandElement loadedChoices(ArgKeys argName) {
        return ChoicesArguments.choices(
                argName,
                () -> toMap(FinderWorld.getLoadedWorlds()),
                false);
    }

    private static Map<String, World> toMap(Collection<World> collection) {
        return collection
                .stream()
                .distinct()
                .collect(Collectors.toMap(World::getName, Function.identity()));
    }
}
