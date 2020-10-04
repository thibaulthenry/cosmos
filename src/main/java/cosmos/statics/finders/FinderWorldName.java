package cosmos.statics.finders;

import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinderWorldName {

    private static Collection<String> getAllWorldNames(boolean includeDisabled) {
        return FinderWorldProperties.getAllWorldProperties(includeDisabled)
                .stream()
                .map(WorldProperties::getWorldName)
                .collect(Collectors.toList());
    }

    public static Collection<String> getLoadedWorldNames() {
        return FinderWorld.getLoadedWorlds()
                .stream()
                .map(World::getName)
                .collect(Collectors.toList());
    }

    public static Collection<String> getUnloadedWorldNames(boolean includeDisabled) {
        return FinderWorldProperties.getUnloadedWorldProperties(includeDisabled)
                .stream()
                .map(WorldProperties::getWorldName)
                .collect(Collectors.toList());
    }

    public static Collection<String> getDisabledWorldNames() {
        Collection<String> allWorldsWithoutDisabled = getAllWorldNames(false);
        return getNonDefaultWorldNames(FinderFile::isWorldDirectory, FinderWorldName::isImported)
                .stream()
                .filter(importedWorldName -> !allWorldsWithoutDisabled.contains(importedWorldName))
                .collect(Collectors.toList());
    }

    public static Collection<String> getExportedWorldNames() {
        return getNonDefaultWorldNames(FinderFile::isWorldDirectory, worldName -> !isImported(worldName));
    }

    @SafeVarargs
    private static Collection<String> getNonDefaultWorldNames(Predicate<String>... worldNameFilters) {
        return FinderFile.getDefaultWorldPath().map(defaultWorldPath -> {
            try (Stream<Path> nonDefaultWorldPath = Files.walk(defaultWorldPath)) {
                return nonDefaultWorldPath
                        .filter(path -> path.toFile().isDirectory())
                        .filter(path -> path.getParent() != null && path.getParent().equals(defaultWorldPath))
                        .map(directory -> directory.getFileName().toString())
                        .filter(fileName -> Stream.of(worldNameFilters)
                                .allMatch(worldNameFilter -> worldNameFilter.test(fileName)))
                        .collect(Collectors.toList());
            } catch (UnsupportedOperationException | SecurityException | IOException ignored) {
                return Collections.<String>emptyList();
            }
        }).orElse(Collections.emptyList());
    }

    public static boolean isImported(String worldName) {
        return FinderFile.getWorldSpongeDataPath(worldName)
                .map(FinderFile::doesFileExist)
                .orElse(false);
    }

    public static boolean isLoaded(String worldName) {
        return FinderWorld.getLoadedWorlds()
                .stream()
                .map(World::getName)
                .anyMatch(worldName::equals);
    }
}
