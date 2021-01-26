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
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final FinderService finderService;

    @Inject
    public BackupServiceImpl(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
    }

    private final List<String> backupDirs = Arrays.asList("data", "region");

    private void copyDirectories(final Path sourcePath, final Path targetPath) throws IOException {
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (backupDirs.contains(file.getParent().getFileName().toString())) {
                    Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void delete(final BackupArchetype backupArchetype) throws IOException {
        final Path backupPath = this.finderService.getBackupPath(backupArchetype)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while deleting backup"));
        Files.deleteIfExists(backupPath);
    }

    private void emptyDirectories(final Path targetPath) throws IOException {
        Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (backupDirs.contains(file.getParent().getFileName().toString())) {
                    Files.deleteIfExists(file);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private Optional<BackupArchetype> find(final Path backupDirectory) {
        try {
            return this.finderService.readFromFile(backupDirectory.resolve(BACKUP_NBT_FILE))
                    .flatMap(view -> Sponge.getDataManager().deserialize(BackupArchetypeData.class, view))
                    .flatMap(BackupArchetypeData::collect);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An expected error occurred while creating backup data from directory " + backupDirectory, e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, BackupArchetype> getBackupMap() {
        return this.stream()
                .distinct()
                .collect(Collectors.toMap(BackupArchetype::getName, Function.identity(), (e1, e2) -> e1));
    }

    @Override
    public List<BackupArchetype> getBackups() {
        return this.stream().collect(Collectors.toList());
    }

    @Override
    public Map<String, ResourceKey> getBackupWorldMap() {
        return this.stream()
                .map(BackupArchetype::getWorldKey)
                .distinct()
                .collect(Collectors.toMap(ResourceKey::getFormatted, Function.identity(), (e1, e2) -> e1));
    }

    @Override
    public boolean hasBackup(final ResourceKey key) {
        return this.getBackups()
                .stream()
                .map(BackupArchetype::getWorldKey)
                .anyMatch(backupWorldKey -> backupWorldKey.equals(key));
    }

    private boolean isBackup(final Path backupPath) {
        return this.finderService.getCosmosPath(Directories.BACKUPS_DIRECTORY_NAME)
                .map(backupsDirectory -> {
                    try {
                        final boolean isDirectory = backupPath.toFile().isDirectory();
                        final boolean isInBackupsDirectory = backupPath.getParent() != null
                                && backupPath.getParent().equals(backupsDirectory);
                        final boolean hasBackupDirectories = this.backupDirs
                                .stream()
                                .allMatch(backupDirectory -> backupPath.resolve(backupDirectory).toFile().exists());
                        return isDirectory && isInBackupsDirectory && hasBackupDirectories;
                    } catch (final Exception e) {
                        Cosmos.getLogger().warn("An expected error occurred while checking backup", e);
                        return false;
                    }
                }).orElse(false);
    }

    @Override
    public void restore(final BackupArchetype backupArchetype) throws IOException {
        final Path backupPath = this.finderService.getBackupPath(backupArchetype)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while restoring backup"));
        final Path worldPath = this.finderService.getWorldPath(backupArchetype.getWorldKey())
                .orElseThrow(() -> new IOException("Unable to find world directory while restoring backup"));
        this.emptyDirectories(worldPath);
        this.copyDirectories(backupPath, worldPath);
    }

    @Override
    public void save(final BackupArchetype backupArchetype) throws IOException {
        final Path worldPath = this.finderService.getWorldPath(backupArchetype.getWorldKey())
                .orElseThrow(() -> new IOException("Unable to find world directory while saving backup"));
        final Path backupPath = this.finderService.getBackupPath(backupArchetype)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while saving backup"));
        this.copyDirectories(worldPath, backupPath);
        this.finderService.writeToFile(new BackupArchetypeData(backupArchetype), backupPath.resolve(BACKUP_NBT_FILE));
    }

    private Stream<BackupArchetype> stream() {
        return this.finderService.getCosmosPath(Directories.BACKUPS_DIRECTORY_NAME).map(backupDirectory -> {
            try {
                return Files.walk(backupDirectory)
                        .filter(this::isBackup)
                        .map(this::find)
                        .filter(Optional::isPresent)
                        .map(Optional::get);
            } catch (final Exception e) {
                Cosmos.getLogger().warn("An unexpected error occurred while looking for existing backups", e);
                return Stream.<BackupArchetype>empty();
            }
        }).orElse(Stream.empty());
    }

    @Override
    public void tag(final BackupArchetype backupArchetype, final String tag) throws IOException {
        final Path backupPath = this.finderService.getBackupPath(backupArchetype)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while tagging backup"));

        if (!this.isBackup(backupPath)) {
            throw new IOException("Unable to find world backup directory while tagging backup");
        }

        backupArchetype.setTag(tag);
        this.finderService.writeToFile(new BackupArchetypeData(backupArchetype), backupPath.resolve(BACKUP_NBT_FILE));
    }

}
