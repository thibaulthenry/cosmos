package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.BackupServiceImpl;
import org.spongepowered.api.ResourceKey;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ImplementedBy(BackupServiceImpl.class)
public interface BackupService extends CosmosService {

    void delete(BackupArchetype backupArchetype) throws IOException;

    Map<String, BackupArchetype> getBackupMap();

    List<BackupArchetype> getBackups();

    Map<String, ResourceKey> getBackupWorldMap();

    boolean hasBackup(ResourceKey key);

    void restore(BackupArchetype backupArchetype) throws IOException;

    void save(BackupArchetype backupArchetype) throws IOException;

    void tag(BackupArchetype backupArchetype, String tag) throws IOException;

}
