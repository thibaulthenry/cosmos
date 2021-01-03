package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.BackupServiceImpl;
import org.spongepowered.api.ResourceKey;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ImplementedBy(BackupServiceImpl.class)
public interface BackupService extends CosmosService {

    List<BackupArchetype> getBackups();

    Map<String, BackupArchetype> getBackupMap();

    List<ResourceKey> getBackupWorlds();

    Map<String, ResourceKey> getBackupWorldMap();

    boolean hasBackup(ResourceKey key);

    void tag(BackupArchetype backupArchetype) throws IOException;

    void save(BackupArchetype backupArchetype) throws IOException;

    void restore(BackupArchetype backupArchetype) throws IOException;

    void delete(BackupArchetype backupArchetype) throws IOException;

}
