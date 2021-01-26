package cosmos.services.io.impl;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.Directories;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.io.FinderService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataSerializable;
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
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class FinderServiceImpl implements FinderService {

    @Override
    public void deletePerWorldFiles(final ResourceKey worldKey) throws IOException {
        Optional<Path> optionalPath;

        for (final String directoryName : this.getPerWorldDirectories()) {
            optionalPath = this.getCosmosPath(directoryName, worldKey.namespace(), worldKey.value());

            if (optionalPath.isPresent()) {
                Files.deleteIfExists(optionalPath.get());
            }
        }
    }

    @Override
    public Optional<Path> getBackupPath(final BackupArchetype backupArchetype) {
        final String directory = backupArchetype.getWorldKey().getFormatted().replaceAll(":", "_")
                + "_" + backupArchetype.getCreationDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        return this.getCosmosPath(Directories.BACKUPS_DIRECTORY_NAME, directory);
    }

    @Override
    public Optional<Path> getBackupsPath() {
        return this.getCosmosPath(Directories.BACKUPS_DIRECTORY_NAME);
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
    public Optional<Path> getWorldPath(ResourceKey worldKey) {
        return Optional.empty(); // todo
    }

    private Optional<Path> getPath(final String first, final String... subs) {
        try {
            return Optional.of(Paths.get(first, subs));
        } catch (final Exception ignored) {
            return Optional.empty();
        }
    }

    private Collection<String> getPerWorldDirectories() {
        return Stream.of(
                Directories.ADVANCEMENTS_DIRECTORY_NAME,
                Directories.EXPERIENCES_DIRECTORY_NAME,
                Directories.HEALTHS_DIRECTORY_NAME,
                Directories.HUNGERS_DIRECTORY_NAME,
                Directories.INVENTORIES_DIRECTORY_NAME,
                Directories.SCOREBOARDS_DIRECTORY_NAME
        ).collect(Collectors.toList());
    }

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

    @Override
    public Optional<DataContainer> readFromFile(final Path path) {
        if (path == null || !path.toFile().exists()) {
            return Optional.empty();
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            final DataContainer dataContainer = DataFormats.NBT.get().readFrom(inputStream);

            if (dataContainer.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(dataContainer);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An error occurred while reading data container at " + path, e);
            return Optional.empty();
        }
    }

    @Override
    public void writeToFile(final DataSerializable dataSerializable, final Path path) {
        this.writeToFile(dataSerializable.toContainer(), path);
    }

    private void writeToFile(final DataView dataContainer, final Path path) {
        if (dataContainer == null || dataContainer.isEmpty()) {
            return;
        }

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            DataFormats.NBT.get().writeTo(outputStream, dataContainer);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An error occurred while saving data container at " + path, e);
        }
    }

}
