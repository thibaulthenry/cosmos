package cosmos.services.io.impl;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.models.backup.BackupArchetype;
import cosmos.models.enums.Directories;
import cosmos.services.io.FinderService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataView;

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

@Singleton
public class FinderServiceImpl implements FinderService {

    @Override
    public boolean initDirectories() {
        return Stream.of(
                Directories.ADVANCEMENTS_DIRECTORY_NAME,
                Directories.BACKUPS_DIRECTORY_NAME,
                Directories.EXPERIENCES_DIRECTORY_NAME,
                Directories.HEALTHS_DIRECTORY_NAME,
                Directories.HUNGERS_DIRECTORY_NAME,
                Directories.INVENTORIES_DIRECTORY_NAME,
                Directories.SCOREBOARDS_DIRECTORY_NAME
        ).allMatch(this::initDirectory);
    }

    private boolean initDirectory(final String directoryName) {
        return this.getCosmosPath(directoryName).map(path -> {
            try {
                final File directory = path.toFile();

                if (!directory.isDirectory()) {
                    return directory.mkdirs();
                }

                return true;
            } catch (final Exception ignored) {
                return false;
            }
        }).orElse(false);
    }

    private Optional<Path> getPath(final String first, final String... subs) {
        try {
            return Optional.of(Paths.get(first, subs));
        } catch (final Exception ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Path> getConfigPath(final String... subs) {
        return this.getPath(Directories.COSMOS_CONFIG_DIRECTORY_NAME, subs);
    }

    @Override
    public Optional<Path> getCosmosPath(final String... subs) {
        return this.getPath(Directories.COSMOS_MODS_DIRECTORY_NAME, subs);
    }

    @Override
    public Optional<Path> getBackupsPath() {
        return this.getCosmosPath(Directories.BACKUPS_DIRECTORY_NAME);
    }

    @Override
    public Optional<Path> getBackupPath(final BackupArchetype backupArchetype, final boolean useTag) {
        String directory = backupArchetype.getWorldKey().getFormatted() + "_"
                + backupArchetype.getCreationDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "_"
                + backupArchetype.getUuid();

        final Optional<String> optionalTag = backupArchetype.getTag();

        if (useTag && optionalTag.isPresent()) {
            directory = directory + "_" + optionalTag.get();
        }

        return this.getCosmosPath(Directories.BACKUPS_DIRECTORY_NAME, directory);
    }

    @Override
    public Optional<Path> getDefaultWorldPath() {
        return this.getPath(Sponge.getGame().getGameDirectory().toString(), Sponge.getServer().getDefaultWorldKey().getFormatted());
    }

    @Override
    public Optional<Path> getWorldPath(final ResourceKey worldKey) {
        return this.getDefaultWorldPath().flatMap(path -> this.getPath(path.toString(), worldKey.getFormatted()));
    }

    private Optional<Path> getWorldDataPath(final ResourceKey worldKey) {
        return this.getDefaultWorldPath().flatMap(path -> this.getPath(path.toString(), worldKey.getFormatted(), "level.dat"));
    }

    @Override
    public Optional<Path> getWorldSpongeDataPath(final ResourceKey worldKey) {
        return this.getDefaultWorldPath().flatMap(path -> this.getPath(path.toString(), worldKey.getFormatted(), "level_sponge.dat"));
    }

    @Override
    public boolean isWorldDirectory(final ResourceKey worldKey) {
        return this.getWorldDataPath(worldKey)
                .map(this::doesFileExist)
                .orElse(false);
    }

    @Override
    public boolean doesFileExist(final Path path) {
        try {
            return path.toFile().exists();
        } catch (final Exception ignored) {
            return false;
        }
    }

    @Override
    public void deleteDirectory(final Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void exportWorld(final ResourceKey worldKey) {
        final Optional<Path> optionalWorldPath = this.getWorldPath(worldKey);

        if (!optionalWorldPath.isPresent()) {
            return;
        }

        final String worldPathString = optionalWorldPath.get().toString();
        final Optional<Path> optionalLevelSpongeDatPath = this.getPath(worldPathString, "level_sponge.dat");
        final Optional<Path> optionalLevelSpongeDatOldPath = this.getPath(worldPathString, "level_sponge.dat_old");

        try {
            if (optionalLevelSpongeDatPath.isPresent()) {
                Files.deleteIfExists(optionalLevelSpongeDatPath.get());
            }

            if (optionalLevelSpongeDatOldPath.isPresent()) {
                Files.deleteIfExists(optionalLevelSpongeDatOldPath.get());
            }
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while exporting world", e);
        }
    }

    @Override
    public void deleteWorldFiles(final String uuid) throws IOException {
        final Optional<Path> optionalCosmosPath = this.getCosmosPath();

        if (!optionalCosmosPath.isPresent()) {
            return;
        }

        final String safeUuid = uuid + "-";

        Files.walkFileTree(optionalCosmosPath.get(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
                if (path.getFileName().toString().contains(safeUuid)) {
                    Files.deleteIfExists(path);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void writeToFile(final DataView dataContainer, final Path path) {
        if (dataContainer == null || dataContainer.isEmpty()) {
            return;
        }

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            DataFormats.NBT.get().writeTo(outputStream, dataContainer);
        } catch (final Exception ignored) {
            Cosmos.getLogger().error("An error occurred while saving data container at " + path);
        }
    }

    @Override
    public Optional<DataContainer> readFromFile(final Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            final DataContainer dataContainer = DataFormats.NBT.get().readFrom(inputStream);

            if (dataContainer.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(dataContainer);
        } catch (final Exception ignored) {
            Cosmos.getLogger().error("An error occurred while reading data container at " + path);
            return Optional.empty();
        }
    }
}
