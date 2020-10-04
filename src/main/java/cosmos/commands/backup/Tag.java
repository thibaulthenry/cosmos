package cosmos.commands.backup;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.Units;
import cosmos.models.BackupArchetype;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.finders.FinderBackup;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;

public class Tag extends AbstractCommand {

    public Tag() {
        super(
                Arguments.limitCompleteElement(
                        WorldNameArguments.backupChoices(ArgKeys.WORLD_BACKUP)
                ),
                GenericArguments.string(ArgKeys.TAG.t)
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        BackupArchetype backupArchetype = args.<BackupArchetype>getOne(ArgKeys.WORLD_BACKUP.t)
                .orElseThrow(Outputs.INVALID_WORLD_BACKUP_CHOICE.asSupplier());

        String tag = args.<String>getOne(ArgKeys.TAG.t)
                .map(input -> input.replaceAll("_", "-"))
                .orElseThrow(Outputs.INVALID_VALUE.asSupplier());

        if (Validator.doesOverflowMaxLength(tag, Units.NAME_MAX_LENGTH)) {
            throw Outputs.TOO_LONG_BACKUP_TAG.asException(tag);
        }

        backupArchetype.setTag(tag);

        try {
            FinderBackup.tag(backupArchetype);
        } catch (Exception ignored) {
            throw Outputs.TAGGING_WORLD_BACKUP.asException(backupArchetype.getWorldName());
        }

        src.sendMessage(Outputs.TAG_WORLD_BACKUP.asText(backupArchetype.getWorldName(), tag));
    }

}
