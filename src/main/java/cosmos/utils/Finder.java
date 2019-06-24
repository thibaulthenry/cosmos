package cosmos.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Finder {

    private static File backupsDirectory;

    public static boolean initBackupsFolders() {
        backupsDirectory = new File("mods/cosmos/backups/");

        if (!backupsDirectory.isDirectory()) {
            return backupsDirectory.mkdirs();
        }

        return true;
    }

    public static Path getBackupPath(boolean isFolder, String worldName) {
        return isFolder ? Paths.get(backupsDirectory.getPath()) : Paths.get(backupsDirectory.getPath(), worldName);
    }

    public static Path getBackupZip(String worldName) {
        return Paths.get(backupsDirectory.getPath(), worldName + ".zip");
    }

    public static Path getDefaultWorldPath() {
        return getWorldFilePath(Sponge.getServer().getDefaultWorldName(), true);
    }

    public static Path getDefaultWorldDataPath() {
        return getWorldFilePath(Sponge.getServer().getDefaultWorldName(), true, "level.dat");
    }

    public static Path getWorldPath(String worldName) {
        return getWorldFilePath(worldName, false);
    }

    public static Path getWorldDataPath(String worldName) {
        return getWorldFilePath(worldName, false, "level.dat");
    }

    public static Path getWorldSpongeDataPath(String worldName) {
        return getWorldFilePath(worldName, false, "level_sponge.dat");
    }

    private static Path getWorldFilePath(String worldName, boolean isDefault, String... fileName) {
        return Paths.get(Sponge.getGame().getGameDirectory().toAbsolutePath() +
                (isDefault ? File.separator + worldName : File.separator + Sponge.getServer().getDefaultWorldName()) +
                (isDefault ? "" : File.separator + worldName) +
                (fileName.length > 0 ? File.separator + fileName[0] : "")
        );
    }

    public static List<String> getAvailableWorldNames(boolean imported) {
        try (Stream<Path> paths = Files.walk(getDefaultWorldPath())) {
            return paths
                    .filter(path -> new File(path.toString()).isDirectory())
                    .map(path -> path.getFileName().toString())
                    .filter(fileName -> isAvailable(fileName) && (imported == isImported(fileName)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getUnloadedWorldNames() {
        return Sponge.getServer().getUnloadedWorlds()
                .stream()
                .map(WorldProperties::getWorldName)
                .collect(Collectors.toList());
    }

    public static List<String> getLoadedWorldNames() {
        return Sponge.getServer().getWorlds()
                .stream()
                .map(World::getName)
                .collect(Collectors.toList());
    }

    public static List<String> getBackupNames() {
        try (Stream<Path> paths = Files.walk(backupsDirectory.toPath())) {
            return paths
                    .map(path -> path.getFileName().toString())
                    .filter(Zip::isZip)
                    .map(fileName -> fileName.substring(0, fileName.length() - 4))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static Map<String, String> toMap(List<String> stringList) {
        return stringList
                .stream()
                .distinct()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
    }

    public static boolean isAvailable(String name) {
        return new File(getWorldDataPath(name).toString()).exists();
    }

    private static boolean isImported(String name) {
        return new File(getWorldSpongeDataPath(name).toString()).exists();
    }
}
