package cosmos.executors.commands.backup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.backup.Backup;
import cosmos.registries.backup.BackupArchetype;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Restore extends AbstractCommand {

    @Inject
    public Restore(final Injector injector) {
        super(injector.getInstance(Backup.class).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final BackupArchetype backupArchetype = context.getOne(CosmosKeys.BACKUP)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.backup", "param", CosmosKeys.BACKUP));

        super.serviceProvider.world().restore(src, backupArchetype, false);

        super.serviceProvider.message()
                .getMessage(src, "success.backup.restore")
                .replace("backup", backupArchetype)
                .green()
                .sendTo(src);
    }

}
