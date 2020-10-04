package cosmos.statics.finders;

import cosmos.models.BackupArchetype;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
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

public class FinderBackup {

    private static final List<String> backupDirs = Arrays.asList("data", "playerdata", "region");

    public static List<BackupArchetype> getBackups() {
        return FinderFile.getCosmosPath(FinderFile.BACKUPS_DIRECTORY_NAME).map(backupDirectory -> {
            try (Stream<Path> paths = Files.walk(backupDirectory)) {
                return paths
                        .filter(FinderBackup::isBackup)
                        .map(BackupArchetype::fromDirectory)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            } catch (IOException ignored) {
                return Collections.<BackupArchetype>emptyList();
            }
        }).orElse(Collections.emptyList());
    }

    public static boolean hasBackup(String worldUUID) {
        return getBackups()
                .stream()
                .map(BackupArchetype::getWorldUUID)
                .anyMatch(backupWorldUUID -> backupWorldUUID.equals(worldUUID));
    }

    public static void tag(BackupArchetype backupArchetype) throws IOException {
        Path untaggedBackupPath = FinderFile.getBackupPath(backupArchetype, false)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while tagging backup"));

        Path taggedBackupPath = FinderFile.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while tagging backup"));

        Path backupsDir = FinderFile.getBackupsPath()
                .orElseThrow(() -> new IOException("Unable to find backups directory while tagging backup"));

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(backupsDir, untaggedBackupPath.getFileName().toString() + "*")) {
            for (Path path : dirStream) {
                Files.move(path, taggedBackupPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public static void save(BackupArchetype backupArchetype) throws IOException {
        Path worldPath = FinderFile.getWorldPath(backupArchetype.getWorldName())
                .orElseThrow(() -> new IOException("Unable to find world directory while saving backup"));
        Path backupPath = FinderFile.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while saving backup"));
        copyDirectories(worldPath, backupPath);
    }

    public static void restore(BackupArchetype backupArchetype) throws IOException {
        Path backupPath = FinderFile.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while restoring backup"));
        Path worldPath = FinderFile.getWorldPath(backupArchetype.getWorldName())
                .orElseThrow(() -> new IOException("Unable to find world directory while restoring backup"));
        emptyDirectories(worldPath);
        copyDirectories(backupPath, worldPath);
    }

    public static void delete(BackupArchetype backupArchetype) throws IOException {
        Path backupPath = FinderFile.getBackupPath(backupArchetype, true)
                .orElseThrow(() -> new IOException("Unable to find world backup directory while deleting backup"));
        FinderFile.deleteDirectory(backupPath);
    }

    private static void copyDirectories(Path sourcePath, Path targetPath) throws IOException {
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (backupDirs.contains(file.getParent().getFileName().toString())) {
                    Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void emptyDirectories(Path targetPath) throws IOException {
        Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (backupDirs.contains(file.getParent().getFileName().toString())) {
                    Files.deleteIfExists(file);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static boolean isBackup(Path backupPath) {
        return FinderFile.getCosmosPath(FinderFile.BACKUPS_DIRECTORY_NAME)
                .map(backupsDirectory -> {
                    try {
                        boolean isDirectory = backupPath.toFile().isDirectory();
                        boolean isInBackupsDirectory = backupPath.getParent() != null &&
                                backupPath.getParent().equals(backupsDirectory);
                        boolean hasBackupDirectories = backupDirs
                                .stream()
                                .allMatch(backupDirectory -> backupPath.resolve(backupDirectory).toFile().exists());
                        return isDirectory && isInBackupsDirectory && hasBackupDirectories;
                    } catch (UnsupportedOperationException | SecurityException | InvalidPathException ignored) {
                        return false;
                    }
                }).orElse(false);
    }
}