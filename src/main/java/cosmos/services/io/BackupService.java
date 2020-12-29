package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.models.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.BackupServiceImpl;

import java.io.IOException;
import java.util.List;

@ImplementedBy(BackupServiceImpl.class)
public interface BackupService extends CosmosService {

    List<BackupArchetype> getBackups();

    boolean hasBackup(String uuid);

    void tag(BackupArchetype backupArchetype) throws IOException;

    void save(BackupArchetype backupArchetype) throws IOException;

    void restore(BackupArchetype backupArchetype) throws IOException;

    void delete(BackupArchetype backupArchetype) throws IOException;

}
