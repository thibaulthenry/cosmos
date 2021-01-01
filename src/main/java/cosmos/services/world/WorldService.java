package cosmos.services.world;

import com.google.inject.ImplementedBy;
import cosmos.models.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.world.impl.WorldServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorldProperties;

import java.util.Optional;

@ImplementedBy(WorldServiceImpl.class)
public interface WorldService extends CosmosService {

    Optional<ServerWorldProperties> findProperties(Audience src);

    ServerWorldProperties getProperties(CommandContext context) throws CommandException;

    boolean isImported(final ResourceKey worldKey);

    boolean isLoaded(ResourceKey worldKey);

    boolean isLoaded(ServerWorldProperties properties);

    void load(Audience src, ResourceKey worldKey, boolean verifyUnloaded) throws CommandException;

    void load(Audience src, ServerWorldProperties properties, boolean verifyUnloaded) throws CommandException;

    void restore(Audience src, BackupArchetype backupArchetype, boolean verifyExistence) throws CommandException;

    void saveProperties(Audience src, ServerWorldProperties worldProperties) throws CommandException;

    void unload(Audience src, ServerWorldProperties properties, boolean verifyLoaded) throws CommandException;

}
