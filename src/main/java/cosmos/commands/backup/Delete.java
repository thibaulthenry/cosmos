package cosmos.commands.backup;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.models.BackupArchetype;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.finders.FinderBackup;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

public class Delete extends AbstractCommand {

    public Delete() {
        super(
                Arguments.limitCompleteElement(
                        WorldNameArguments.backupChoices(ArgKeys.WORLD_BACKUP)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        BackupArchetype backupArchetype = args.<BackupArchetype>getOne(ArgKeys.WORLD_BACKUP.t)
                .orElseThrow(Outputs.INVALID_WORLD_BACKUP_CHOICE.asSupplier());

        try {
            FinderBackup.delete(backupArchetype);
        } catch (Exception ignored) {
            throw Outputs.DELETING_WORLD_BACKUP.asException(backupArchetype.getWorldName());
        }

        src.sendMessage(Outputs.DELETE_WORLD_BACKUP.asText(backupArchetype.getWorldName()));
    }

}
