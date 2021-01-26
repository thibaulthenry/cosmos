package cosmos.executors.commands.backup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.backup.Backup;
import cosmos.registries.backup.BackupArchetype;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Delete extends AbstractCommand {

    @Inject
    public Delete(final Injector injector) {
        super(injector.getInstance(Backup.class).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final BackupArchetype backupArchetype = context.getOne(CosmosKeys.BACKUP)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.backup", "param", CosmosKeys.BACKUP));

        try {
            super.serviceProvider.backup().delete(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while deleting backup", e);
            throw super.serviceProvider.message().getError(src, "error.backup.delete", "backup", backupArchetype);
        }

        super.serviceProvider.message()
                .getMessage(src, "success.backup.delete")
                .replace("backup", backupArchetype)
                .green()
                .sendTo(src);
    }

}
