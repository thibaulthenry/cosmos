package cosmos.services.world;

import com.google.inject.ImplementedBy;
import cosmos.models.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.world.impl.WorldServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;

@ImplementedBy(WorldServiceImpl.class)
public interface WorldService extends CosmosService {

    boolean isImported(final ResourceKey worldKey);

    boolean isLoaded(ResourceKey worldKey);

    void restore(Audience src, BackupArchetype backupArchetype, boolean verifyExistence) throws CommandException;
}
