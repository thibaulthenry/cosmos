package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.models.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.FinderServiceImpl;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@ImplementedBy(FinderServiceImpl.class)
public interface FinderService extends CosmosService {

    boolean initDirectories();

    Optional<Path> getConfigPath(String... subs);

    Optional<Path> getCosmosPath(String... subs);

    Optional<Path> getBackupsPath();

    Optional<Path> getBackupPath(BackupArchetype backupArchetype, boolean useTag);

    Optional<Path> getDefaultWorldPath();

    Optional<Path> getWorldPath(ResourceKey worldKey);

    Optional<Path> getWorldSpongeDataPath(ResourceKey worldKey);

    boolean isWorldDirectory(ResourceKey worldKey);

    boolean doesFileExist(Path path);

    void deleteDirectory(Path path) throws IOException;

    void exportWorld(ResourceKey worldKey);

    void deleteWorldFiles(String uuid) throws IOException;

    void writeToFile(DataView dataContainer, Path path);

    Optional<DataContainer> readFromFile(Path path);
}
