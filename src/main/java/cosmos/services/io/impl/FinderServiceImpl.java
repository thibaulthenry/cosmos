package cosmos.services.io.impl;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.Directories;
import cosmos.services.io.FinderService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class FinderServiceImpl implements FinderService {

    private Collection<String> cosmosDirectories() {
        return Stream.of(
                Directories.ADVANCEMENTS,
                Directories.BACKUPS,
                Directories.EXPERIENCES,
                Directories.HEALTHS,
                Directories.HUNGERS,
                Directories.INVENTORIES,
                Directories.PORTALS,
                Directories.SCOREBOARDS
        ).collect(Collectors.toList());
    }

    @Override
    public void deletePerWorldFiles(final ResourceKey worldKey) throws IOException {
        Optional<Path> optionalPath;

        for (final String directoryName : this.perWorldDirectories()) {
            optionalPath = this.findCosmosPath(directoryName, worldKey);

            if (optionalPath.isPresent()) {
                Files.deleteIfExists(optionalPath.get());
            }
        }
    }

    @Override
    public Optional<Path> findConfigPath(final String... subs) {
        return this.path(Directories.COSMOS_CONFIG, subs);
    }

    public Optional<Path> findCosmosPath(final String... subs) {
        return this.path(Directories.COSMOS, subs);
    }

    @Override
    public Optional<Path> findCosmosPath(final String directory, final ResourceKey worldKey) {
        if (!this.isCosmosDirectory(directory)) {
            return Optional.empty();
        }

        return this.findCosmosPath(directory, worldKey.namespace(), worldKey.value() + ".dat");
    }

    @Override
    public Optional<Path> findCosmosPath(final String directory, final ServerPlayer player) {
        return this.findCosmosPath(directory, player.world(), player);
    }

    @Override
    public Optional<Path> findCosmosPath(final String directory, final ResourceKey worldKey, final ServerPlayer player) {
        if (!this.isCosmosDirectory(directory)) {
            return Optional.empty();
        }

        return this.findCosmosPath(directory, worldKey.namespace(), worldKey.value(), player.uniqueId() + ".dat");
    }

    @Override
    public Optional<Path> findCosmosPath(final String directory, final ServerWorld world, final ServerPlayer player) {
        return this.findCosmosPath(directory, world.key(), player);
    }

    private Optional<Path> path(final String first, final String... subs) {
        try {
            return Optional.of(Paths.get(first, subs));
        } catch (final Exception ignored) {
            return Optional.empty();
        }
    }

    private Collection<String> perWorldDirectories() {
        return Stream.of(
                Directories.ADVANCEMENTS,
                Directories.EXPERIENCES,
                Directories.HEALTHS,
                Directories.HUNGERS,
                Directories.INVENTORIES,
                Directories.SCOREBOARDS
        ).collect(Collectors.toList());
    }

    @Override
    public boolean initDirectories() {
        return Stream.of(
                Directories.ADVANCEMENTS,
                Directories.BACKUPS,
                Directories.EXPERIENCES,
                Directories.HEALTHS,
                Directories.HUNGERS,
                Directories.INVENTORIES,
                Directories.PORTALS,
                Directories.SCOREBOARDS
        ).allMatch(this::initDirectory);
    }

    private boolean initDirectory(final String directoryName) {
        return this.findCosmosPath(directoryName).map(path -> {
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

    private boolean isCosmosDirectory(final String directory) {
        return this.cosmosDirectories().stream().anyMatch(name -> name.equals(directory));
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
            Cosmos.logger().error("An error occurred while reading data container at " + path, e);
            return Optional.empty();
        }
    }

    @Override
    public Stream<Path> stream(final String directory) {
        if (!this.isCosmosDirectory(directory)) {
            return Stream.empty();
        }

        return this.findCosmosPath(directory).map(dir -> {
            try {
                return Files.walk(dir).filter(path -> path.toFile().isFile());
            } catch (final Exception e) {
                Cosmos.logger().error("An unexpected error occurred while walking through Cosmos directory " + directory, e);
                return Stream.<Path>empty();
            }
        }).orElse(Stream.empty());
    }

    @Override
    public Optional<Path> worldPath(final ResourceKey worldKey) {
        return Sponge.server().worldManager().worldDirectory(worldKey);
    }

    @Override
    public void writeToFile(final DataSerializable dataSerializable, final Path path) {
        this.writeToFile(dataSerializable.toContainer(), path);
    }

    private void writeToFile(final DataView dataContainer, final Path path) {
        if (dataContainer == null || dataContainer.isEmpty()) {
            return;
        }

        try {
            final Path parentPath = path.getParent();

            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
        } catch (final Exception e) {
            Cosmos.logger().error("An error occurred while saving data container at " + path, e);
            return;
        }

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            DataFormats.NBT.get().writeTo(outputStream, dataContainer);
        } catch (final Exception e) {
            Cosmos.logger().error("An error occurred while saving data container at " + path, e);
        }
    }

}
