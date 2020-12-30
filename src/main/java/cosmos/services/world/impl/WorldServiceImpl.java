package cosmos.services.world.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.models.backup.BackupArchetype;
import cosmos.services.io.BackupService;
import cosmos.services.io.FinderService;
import cosmos.services.message.MessageService;
import cosmos.services.world.WorldService;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.world.server.ServerWorld;

@Singleton
public class WorldServiceImpl implements WorldService {

    @Inject
    private BackupService backupService;

    @Inject
    private FinderService finderService;

    @Inject
    private MessageService messageService;

    @Override
    public boolean isImported(final ResourceKey worldKey) {
        return this.finderService.getWorldSpongeDataPath(worldKey)
                .map(this.finderService::doesFileExist)
                .orElse(false);
    }

    @Override
    public boolean isLoaded(final ResourceKey worldKey) {
        return Sponge.getServer().getWorldManager().getWorlds()
                .stream()
                .map(ServerWorld::getKey)
                .anyMatch(worldKey::equals);
    }

    @Override
    public void restore(final Audience src, final BackupArchetype backupArchetype, final boolean verifyExistence) throws CommandException {
        final ResourceKey defaultWorldKey = Sponge.getServer().getDefaultWorldKey();
        final ResourceKey worldKey = backupArchetype.getWorldKey();

        if (defaultWorldKey.equals(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.default")
                    .replace("world", defaultWorldKey)
                    .errorColor()
                    .asException();
        }

        if (!this.isImported(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.exported")
                    .replace("world", worldKey)
                    .errorColor()
                    .asException();
        }

        if (this.isLoaded(worldKey)) {
            throw this.messageService.getMessage(src, "error.backup.restore.loaded")
                    .replace("world", worldKey)
                    .errorColor()
                    .asException();
        }

        if (verifyExistence && !this.backupService.hasBackup(backupArchetype.getUuid())) {
            throw this.messageService.getMessage(src, "error.missing.backup")
                    .replace("backup", backupArchetype)
                    .errorColor()
                    .asException();
        }

        try {
            this.backupService.restore(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occured while restoring backup", e);
            throw this.messageService.getMessage(src, "error.backup.restore")
                    .replace("backup", backupArchetype)
                    .errorColor()
                    .asException();
        }
    }
}
