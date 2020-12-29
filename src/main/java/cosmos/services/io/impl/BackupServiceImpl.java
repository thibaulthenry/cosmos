package cosmos.services.io.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.models.backup.BackupArchetype;
import cosmos.models.enums.Directories;
import cosmos.services.io.BackupService;
import cosmos.services.io.FinderService;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class BackupServiceImpl implements BackupService {

    @Inject
    private FinderService finderService;

    private final List<String> backupDirs = Arrays.asList("data", "playerdata", "region");

    @Override
    public List<BackupArchetype> getBackups() {
        return this.finderService.getCosmosPath(Directories.BACKUPS_DIRECTORY_NAME).map(backupDirectory -> {
            try (final Stream<Path> paths = Files.walk(backupDirectory)) {
                return paths
                        .filter(this::isBackup)
                        .map(BackupArchetype::fromDirectory)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            } catch (final Exception ignored) {
                return Collections.<BackupArchetype>emptyList();
            }
        }).orElse(Collections.emptyList());
    }

    @Override
    public boolean hasBackup(final String uuid) {
        return this.getBackups()
                .stream()
                .map(BackupArchetype::getWorldUUID)
                .anyMatch(backupWorldUUID -> backupWorldUUID.equals(uuid));
    }

    @Override
    public void tag(BackupArchetype backupArchetype) throws IOException {
        final Path untaggedBackupPath = this.finderService.getBackupPath(backupArchetype, false)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while tagging backup"));
        final Path taggedBackupPath = this.finderService.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while tagging backup"));
        final Path backupsDir = this.finderService.getBackupsPath()
                .orElseThrow(() -> new IOException("Unable to find backups directory while tagging backup"));

        try (final DirectoryStream<Path> dirStream = Files.newDirectoryStream(backupsDir, untaggedBackupPath.getFileName().toString() + "*")) {
            for (final Path path : dirStream) {
                Files.move(path, taggedBackupPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    @Override
    public void save(final BackupArchetype backupArchetype) throws IOException {
        final Path worldPath = this.finderService.getWorldPath(backupArchetype.getWorldKey())
                .orElseThrow(() -> new IOException("Unable to find world directory while saving backup"));
        final Path backupPath = this.finderService.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while saving backup"));
        this.copyDirectories(worldPath, backupPath);
    }

    @Override
    public void restore(final BackupArchetype backupArchetype) throws IOException {
        final Path backupPath = this.finderService.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while restoring backup"));
        final Path worldPath = this.finderService.getWorldPath(backupArchetype.getWorldKey())
                .orElseThrow(() -> new IOException("Unable to find world directory while restoring backup"));
        this.emptyDirectories(worldPath);
        this.copyDirectories(backupPath, worldPath);
    }

    @Override
    public void delete(final BackupArchetype backupArchetype) throws IOException {
        final Path backupPath = this.finderService.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while deleting backup"));
        this.finderService.deleteDirectory(backupPath);
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
                if (backupDirs.contains(file.getParent().getFileName().toString())) {
                    Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                }

                return FileVisitResult.CONTINUE;
            }
        });
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
                    } catch (final Exception ignored) {
                        return false;
                    }
                }).orElse(false);
    }
}