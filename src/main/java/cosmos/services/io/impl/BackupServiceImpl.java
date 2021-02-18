package cosmos.services.io.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.Directories;
import cosmos.registries.backup.BackupArchetype;
import cosmos.registries.data.serializable.impl.BackupArchetypeData;
import cosmos.services.io.BackupService;
import cosmos.services.io.FinderService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class BackupServiceImpl implements BackupService {

    private static final String BACKUP_NBT_FILE = "backup.dat";

    private final List<String> backupDirectories = Arrays.asList("data", "region");
    private final FinderService finderService;

    @Inject
    public BackupServiceImpl(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
    }

    @Override
    public Map<String, BackupArchetype> backupMap() {
        return this.stream()
                .distinct()
                .collect(Collectors.toMap(BackupArchetype::name, Function.identity(), (e1, e2) -> e1));
    }

    @Override
    public List<BackupArchetype> backups() {
        return this.stream().collect(Collectors.toList());
    }

    @Override
    public Map<String, ResourceKey> backupWorldMap() {
        return this.stream()
                .map(BackupArchetype::worldKey)
                .distinct()
                .collect(Collectors.toMap(ResourceKey::formatted, Function.identity(), (e1, e2) -> e1));
    }

    private void copyDirectories(final Path sourcePath, final Path targetPath) throws IOException {
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (backupDirectories.contains(file.getParent().getFileName().toString())) {
                    Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void delete(final BackupArchetype backupArchetype) throws IOException {
        final Path backupPath = this.finderService.findCosmosPath(Directories.BACKUPS, backupArchetype.worldKey())
                .orElseThrow(() -> new IOException("Unable to find world backup directory while deleting backup"));

        Files.deleteIfExists(backupPath);
    }

    private void emptyDirectories(final Path targetPath) throws IOException {
        Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (backupDirectories.contains(file.getParent().getFileName().toString())) {
                    Files.deleteIfExists(file);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private Optional<BackupArchetype> find(final Path backupDirectory) {
        try {
            return this.finderService.readFromFile(backupDirectory.resolve(BACKUP_NBT_FILE))
                    .flatMap(view -> Sponge.dataManager().deserialize(BackupArchetypeData.class, view))
                    .flatMap(BackupArchetypeData::collect);
        } catch (final Exception e) {
            Cosmos.logger().error("An expected error occurred while creating backup data from directory " + backupDirectory, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean hasBackup(final ResourceKey key) {
        return this.backups()
                .stream()
                .map(BackupArchetype::worldKey)
                .anyMatch(backupWorldKey -> backupWorldKey.equals(key));
    }

    private boolean isBackup(final Path backupPath) {
        return this.finderService.findCosmosPath(Directories.BACKUPS)
                .map(backupsDirectory -> {
                    try {
                        final boolean isDirectory = backupPath.toFile().isDirectory();

                        final boolean isInBackupsDirectory = backupPath.getParent() != null
                                && backupPath.getParent().equals(backupsDirectory);

                        final boolean hasBackupDirectories = this.backupDirectories
                                .stream()
                                .allMatch(backupDirectory -> backupPath.resolve(backupDirectory).toFile().exists());

                        return isDirectory && isInBackupsDirectory && hasBackupDirectories;
                    } catch (final Exception e) {
                        Cosmos.logger().error("An expected error occurred while checking backup", e);
                        return false;
                    }
                }).orElse(false);
    }

    @Override
    public void restore(final BackupArchetype backupArchetype) throws IOException {
        final Path backupPath = this.finderService.findCosmosPath(Directories.BACKUPS, backupArchetype.worldKey())
                .orElseThrow(() -> new IOException("Unable to find world backup directory while restoring backup"));

        final Path worldPath = this.finderService.worldPath(backupArchetype.worldKey())
                .orElseThrow(() -> new IOException("Unable to find world directory while restoring backup"));

        this.emptyDirectories(worldPath);
        this.copyDirectories(backupPath, worldPath);
    }

    @Override
    public void save(final BackupArchetype backupArchetype) throws IOException {
        final Path worldPath = this.finderService.worldPath(backupArchetype.worldKey())
                .orElseThrow(() -> new IOException("Unable to find world directory while saving backup"));

        final Path backupPath = this.finderService.findCosmosPath(Directories.BACKUPS, backupArchetype.worldKey())
                .orElseThrow(() -> new IOException("Unable to find world backup directory while saving backup"));

        this.copyDirectories(worldPath, backupPath);
        this.finderService.writeToFile(new BackupArchetypeData(backupArchetype), backupPath.resolve(BACKUP_NBT_FILE));
    }

    private Stream<BackupArchetype> stream() {
        return this.finderService.findCosmosPath(Directories.BACKUPS).map(backupDirectory -> {
            try {
                return Files.walk(backupDirectory)
                        .filter(this::isBackup)
                        .map(this::find)
                        .filter(Optional::isPresent)
                        .map(Optional::get);
            } catch (final Exception e) {
                Cosmos.logger().error("An unexpected error occurred while looking for existing backups", e);
                return Stream.<BackupArchetype>empty();
            }
        }).orElse(Stream.empty());
    }

    @Override
    public void tag(final BackupArchetype backupArchetype, final String tag) throws IOException {
        final Path backupPath = this.finderService.findCosmosPath(Directories.BACKUPS, backupArchetype.worldKey())
                .orElseThrow(() -> new IOException("Unable to find world backup directory while tagging backup"));

        if (!this.isBackup(backupPath)) {
            throw new IOException("Unable to find world backup directory while tagging backup");
        }

        backupArchetype.tag(tag);
        this.finderService.writeToFile(new BackupArchetypeData(backupArchetype), backupPath.resolve(BACKUP_NBT_FILE));
    }

}
