package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.FinderServiceImpl;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.data.persistence.DataView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@ImplementedBy(FinderServiceImpl.class)
public interface FinderService extends CosmosService {

    void deletePerWorldFiles(ResourceKey worldKey) throws IOException;

    Optional<Path> getBackupPath(BackupArchetype backupArchetype);

    Optional<Path> getBackupsPath();

    Optional<Path> getConfigPath(String... subs);

    Optional<Path> getCosmosPath(String... subs);

    Optional<Path> getWorldPath(ResourceKey worldKey);

    boolean initDirectories();

    Optional<DataContainer> readFromFile(Path path);

    void writeToFile(DataSerializable dataSerializable, Path path);
}
