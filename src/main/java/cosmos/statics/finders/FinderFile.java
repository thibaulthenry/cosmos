package cosmos.statics.finders;

import cosmos.Cosmos;
import cosmos.models.BackupArchetype;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

public class FinderFile {

    public static final String ADVANCEMENTS_DIRECTORY_NAME = "advancements";
    public static final String ENDER_CHESTS_DIRECTORY_NAME = "enderchests";
    public static final String EXPERIENCES_DIRECTORY_NAME = "experiences";
    public static final String HEALTHS_DIRECTORY_NAME = "healths";
    public static final String HUNGERS_DIRECTORY_NAME = "hungers";
    public static final String INVENTORIES_DIRECTORY_NAME = "inventories";
    public static final String SCOREBOARDS_DIRECTORY_NAME = "scoreboards";
    static final String BACKUPS_DIRECTORY_NAME = "backups";
    private static final String COSMOS_MODS_DIRECTORY_NAME = "mods" + File.separator + "cosmos";
    private static final String COSMOS_CONFIG_DIRECTORY_NAME = "config" + File.separator + "cosmos";

    public static boolean initDirectories() {
        return Stream.of(
                ADVANCEMENTS_DIRECTORY_NAME,
                BACKUPS_DIRECTORY_NAME,
                ENDER_CHESTS_DIRECTORY_NAME,
                EXPERIENCES_DIRECTORY_NAME,
                HEALTHS_DIRECTORY_NAME,
                HUNGERS_DIRECTORY_NAME,
                INVENTORIES_DIRECTORY_NAME,
                SCOREBOARDS_DIRECTORY_NAME
        ).allMatch(FinderFile::initDirectory);
    }

    private static boolean initDirectory(String directoryName) {
        return getCosmosPath(directoryName).map(path -> {
            try {
                File directory = path.toFile();

                if (!directory.isDirectory()) {
                    return directory.mkdirs();
                }

                return true;
            } catch (UnsupportedOperationException | SecurityException ignored) {
                return false;
            }
        }).orElse(false);
    }

    private static Optional<Path> getPath(String first, String... subs) {
        try {
            return Optional.of(Paths.get(first, subs));
        } catch (Exception ignored) {
            String subsPath = subs.length > 0 ? "\\" + String.join("\\", subs) : "";
            Cosmos.sendConsole(Text.of(TextColors.GOLD, "Unable to access file or directory: .\\" + first + subsPath));
            return Optional.empty();
        }
    }

    public static Optional<Path> getConfigPath(String... subs) {
        return getPath(COSMOS_CONFIG_DIRECTORY_NAME, subs);
    }

    public static Optional<Path> getCosmosPath(String... subs) {
        return getPath(COSMOS_MODS_DIRECTORY_NAME, subs);
    }

    public static Optional<Path> getBackupsPath() {
        return getCosmosPath(BACKUPS_DIRECTORY_NAME);
    }

    static Optional<Path> getBackupPath(BackupArchetype backupArchetype, boolean useTag) {
        String directory = backupArchetype.getWorldName() + "_"
                + backupArchetype.getCreationDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "_"
                + backupArchetype.getWorldUUID();

        Optional<String> optionalTag = backupArchetype.getTag();

        if (useTag && optionalTag.isPresent()) {
            directory = directory + "_" + optionalTag.get();
        }

        return getCosmosPath(BACKUPS_DIRECTORY_NAME, directory);
    }

    static Optional<Path> getDefaultWorldPath() {
        return getPath(Sponge.getGame().getGameDirectory().toString(), Sponge.getServer().getDefaultWorldName());
    }

    public static Optional<Path> getWorldPath(String worldName) {
        return getDefaultWorldPath().flatMap(path -> getPath(path.toString(), worldName));
    }

    private static Optional<Path> getWorldDataPath(String worldName) {
        return getDefaultWorldPath().flatMap(path -> getPath(path.toString(), worldName, "level.dat"));
    }

    static Optional<Path> getWorldSpongeDataPath(String worldName) {
        return getDefaultWorldPath().flatMap(path -> getPath(path.toString(), worldName, "level_sponge.dat"));
    }

    public static boolean isWorldDirectory(String name) {
        return getWorldDataPath(name)
                .map(FinderFile::doesFileExist)
                .orElse(false);
    }

    static boolean doesFileExist(Path path) {
        try {
            return path.toFile().exists();
        } catch (UnsupportedOperationException | SecurityException ignored) {
            return false;
        }
    }

    public static void deleteDirectory(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void exportWorld(String worldName) {
        Optional<Path> optionalWorldPath = getWorldPath(worldName);

        if (!optionalWorldPath.isPresent()) {
            return;
        }

        String worldPathString = optionalWorldPath.get().toString();
        Optional<Path> optionalLevelSpongeDatPath = getPath(worldPathString, "level_sponge.dat");
        Optional<Path> optionalLevelSpongeDatOldPath = getPath(worldPathString, "level_sponge.dat_old");

        try {
            if (optionalLevelSpongeDatPath.isPresent()) {
                Files.deleteIfExists(optionalLevelSpongeDatPath.get());
            }

            if (optionalLevelSpongeDatOldPath.isPresent()) {
                Files.deleteIfExists(optionalLevelSpongeDatOldPath.get());
            }
        } catch (IOException ignored) {
        }
    }

    public static void deleteWorldFiles(String uuid) throws IOException {
        Optional<Path> optionalCosmosPath = getCosmosPath();

        if (!optionalCosmosPath.isPresent()) {
            return;
        }

        String safeUuid = uuid + "-";

        Files.walkFileTree(optionalCosmosPath.get(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                if (path.getFileName().toString().contains(safeUuid)) {
                    Files.deleteIfExists(path);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void writeToFile(DataView dataContainer, Path path) {
        if (dataContainer == null || dataContainer.isEmpty()) {
            return;
        }

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            DataFormats.NBT.writeTo(outputStream, dataContainer);
        } catch (IllegalArgumentException | UnsupportedOperationException | IOException | SecurityException ignored) {
            Cosmos.sendConsole("An error occurred while saving data container at " + path);
        }
    }

    public static Optional<DataContainer> readFromFile(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            DataContainer dataContainer = DataFormats.NBT.readFrom(inputStream);

            if (dataContainer.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(dataContainer);
        } catch (IllegalArgumentException | UnsupportedOperationException | IOException | SecurityException ignored) {
            return Optional.empty();
        }
    }
}
