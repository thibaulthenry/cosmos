package cosmos.commands.backup;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.models.BackupArchetype;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.finders.FinderBackup;
import cosmos.statics.finders.FinderWorldName;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

public class Restore extends AbstractCommand {

    public Restore() {
        super(
                Arguments.limitCompleteElement(
                        WorldNameArguments.backupChoices(ArgKeys.WORLD_BACKUP)
                )
        );
    }

    static void restore(BackupArchetype backupArchetype, boolean verifyExistence) throws CommandException {
        String defaultWorldName = Sponge.getServer().getDefaultWorldName();
        String worldName = backupArchetype.getWorldName();

        if (!FinderWorldName.isImported(worldName)) {
            throw Outputs.RESTORING_EXPORTED_WORLD.asException(worldName);
        }

        if (FinderWorldName.isLoaded(worldName)) {
            throw Outputs.RESTORING_LOADED_WORLD.asException(worldName);
        }

        if (defaultWorldName.equals(worldName)) {
            throw Outputs.RESTORING_DEFAULT_WORLD.asException(defaultWorldName);
        }

        if (verifyExistence && !FinderBackup.hasBackup(backupArchetype.getWorldUUID())) {
            throw Outputs.MISSING_WORLD_BACKUP.asException(worldName);
        }

        try {
            FinderBackup.restore(backupArchetype);
        } catch (Exception ignored) {
            throw Outputs.RESTORING_WORLD_BACKUP.asException(worldName);
        }
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        BackupArchetype backupArchetype = args.<BackupArchetype>getOne(ArgKeys.WORLD_BACKUP.t)
                .orElseThrow(Outputs.INVALID_WORLD_BACKUP_CHOICE.asSupplier());

        restore(backupArchetype, false);

        src.sendMessage(Outputs.RESTORE_WORLD_BACKUP.asText(backupArchetype.getWorldName()));
    }
}
