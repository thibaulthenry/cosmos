package cosmos.services.world;

import com.google.inject.ImplementedBy;
import cosmos.registries.backup.BackupArchetype;
import cosmos.services.CosmosService;
import cosmos.services.world.impl.WorldServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Collection;
import java.util.Optional;

@ImplementedBy(WorldServiceImpl.class)
public interface WorldService extends CosmosService {

    void copy(Audience src, ResourceKey worldKey, ResourceKey copyKey, boolean checkNonexistent) throws CommandException;

    void delete(Audience src, ResourceKey worldKey, boolean checkOffline) throws CommandException;

    Optional<ResourceKey> findKeyOrSource(CommandContext context);

    Optional<ResourceKey> findKeyOrSource(CommandContext context, Parameter.Key<ResourceKey> worldKey);

    boolean isImported(ResourceKey worldKey);

    boolean isOnline(ResourceKey worldKey);

    boolean isOffline(ResourceKey worldKey);

    ResourceKey keyOrSource(CommandContext context) throws CommandException;

    ResourceKey keyOrSource(CommandContext context, Parameter.Key<ResourceKey> worldKey) throws CommandException;

    void load(Audience src, ResourceKey worldKey, boolean checkOffline) throws CommandException;

    ServerWorldProperties propertiesOrSource(CommandContext context) throws CommandException;

    void rename(Audience src, ResourceKey worldKey, ResourceKey renameKey, boolean checkNonexistent) throws CommandException;

    void restore(Audience src, BackupArchetype backupArchetype, boolean checkExistent) throws CommandException;

    void saveProperties(Audience src, ServerWorldProperties properties) throws CommandException;

    void unload(Audience src, ResourceKey key, boolean checkOnline) throws CommandException;

    void unload(Audience src, ServerWorld world) throws CommandException;

    Collection<ResourceKey> worldKeysOffline();

}
