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

    Map<String, BackupArchetype> backupMap();

    List<BackupArchetype> backups();

    Map<String, ResourceKey> backupWorldMap();

    void delete(BackupArchetype backupArchetype) throws IOException;

    boolean hasBackup(ResourceKey key);

    void restore(BackupArchetype backupArchetype) throws IOException;

    void save(BackupArchetype backupArchetype) throws IOException;

    void tag(BackupArchetype backupArchetype, String tag) throws IOException;

}
