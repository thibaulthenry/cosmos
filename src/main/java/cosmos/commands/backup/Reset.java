package cosmos.commands.backup;

import cosmos.commands.AbstractCommand;
import cosmos.commands.root.Load;
import cosmos.commands.root.Unload;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.models.BackupArchetype;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Reset extends AbstractCommand {

    public Reset() {
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

        Optional<World> optionalLoadedWorld = backupArchetype.getLoadedWorld();

        if (optionalLoadedWorld.isPresent()) {
            Unload.unload(optionalLoadedWorld.get().getProperties(), false);
        }

        Restore.restore(backupArchetype, true);

        Load.load(backupArchetype.getWorldName(), true);

        src.sendMessage(Outputs.RESET_WORLD_BACKUP.asText(backupArchetype.getWorldName()));
    }
}
