package cosmos.statics.finders;

import cosmos.constants.ArgKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.storage.WorldProperties;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class FinderWorldProperties {

    public static Collection<WorldProperties> getAllWorldProperties(boolean includeDisabled) {
        return Sponge.getServer().getAllWorldProperties()
                .stream()
                .filter(worldProperties -> includeDisabled || worldProperties.isEnabled())
                .collect(Collectors.toList());
    }

    public static Collection<WorldProperties> getLoadedWorldProperties() {
        return FinderWorld.getLoadedWorlds()
                .stream()
                .map(World::getProperties)
                .collect(Collectors.toList());
    }

    public static Collection<WorldProperties> getUnloadedWorldProperties(boolean includeDisabled) {
        return Sponge.getServer()
                .getUnloadedWorlds()
                .stream()
                .filter(worldProperties -> includeDisabled || worldProperties.isEnabled())
                .collect(Collectors.toList());
    }

    public static Optional<WorldProperties> getDisabledWorldProperties(String worldName) {
        if (!FinderWorldName.getDisabledWorldNames().contains(worldName)) {
            return Optional.empty();
        }

        try {
            WorldArchetype worldArchetype = WorldArchetype.builder().build(worldName + Instant.now(), worldName);
            return Optional.of(Sponge.getServer().createWorldProperties(worldName, worldArchetype));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    public static Optional<WorldProperties> getGivenWorldPropertiesOrElse(CommandSource src, CommandContext args, Text key) {
        return args.<WorldProperties>getOne(key)
                .map(Optional::of)
                .orElse(getSourceWorldProperties(src))
                .map(Optional::of)
                .orElse(Sponge.getServer().getDefaultWorld());
    }

    public static Optional<WorldProperties> getGivenWorldPropertiesOrElse(CommandSource src, CommandContext args, ArgKeys argName) {
        return getGivenWorldPropertiesOrElse(src, args, argName.t);
    }

    private static Optional<WorldProperties> getSourceWorldProperties(CommandSource src) {
        return FinderWorld.getSourceWorld(src).map(World::getProperties);
    }

    public static void saveProperties(WorldProperties worldProperties) throws CommandException {
        if (!Sponge.getServer().saveWorldProperties(worldProperties)) {
            throw new CommandException(Text.of("An error occurred while saving ", worldProperties, "'s properties"));
        }
    }

    public static boolean isLoaded(WorldProperties worldProperties) {
        return FinderWorldName.isLoaded(worldProperties.getWorldName());
    }
}
