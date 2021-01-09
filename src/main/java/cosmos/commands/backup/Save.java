package cosmos.commands.backup;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.models.BackupArchetype;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.arguments.WorldPropertiesArguments;
import cosmos.statics.finders.FinderBackup;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class Save extends AbstractCommand {

    public Save() {
        super(
                Arguments.limitCompleteElement(
                        GenericArguments.firstParsing(
                                WorldPropertiesArguments.unloadedChoices(ArgKeys.UNLOADED_WORLD, false),
                                WorldNameArguments.disabledChoices(ArgKeys.DISABLED_WORLD)
                        )
                ),
                GenericArguments.optional(
                        GenericArguments.string(ArgKeys.TAG.t)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties>getOne(ArgKeys.UNLOADED_WORLD.t)
                .map(Optional::of)
                .orElse(
                        args.<String>getOne(ArgKeys.DISABLED_WORLD.t)
                                .flatMap(FinderWorldProperties::getDisabledWorldProperties)
                )
                .orElseThrow(Outputs.INVALID_UNLOADED_DISABLED_WORLD_CHOICE.asSupplier());

        BackupArchetype backupArchetype = new BackupArchetype(worldProperties);
        args.<String>getOne(ArgKeys.TAG.t).ifPresent(backupArchetype::setTag);

        try {
            FinderBackup.save(backupArchetype);
        } catch (Exception ignored) {
            throw Outputs.SAVING_WORLD_BACKUP.asException(worldProperties);
        }

        src.sendMessage(Outputs.SAVE_WORLD_BACKUP.asText(worldProperties));
    }
}
