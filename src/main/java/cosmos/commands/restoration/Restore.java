package cosmos.commands.restoration;

import cosmos.commands.AbstractCommand;
import cosmos.utils.Finder;
import cosmos.utils.Permission;
import cosmos.utils.Zip;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Restore extends AbstractCommand {

    public Restore() {
        super(Permission.COMMAND_RESTORATION_RESTORE);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name").orElseThrow(supplyError("Please insert a valid world name"));

        if (!Zip.doesBackupExists(worldName)) {
            throw new CommandException(Text.of("Unable to find ", worldName, "'s zip backup"));
        }

        if (Sponge.getServer().getDefaultWorldName().equals(worldName)) {
            throw new CommandException(Text.of("Default world cannot be overwritten with a backup"));
        }

        if (Sponge.getServer().getWorld(worldName).isPresent()) {
            throw new CommandException(Text.of(worldName, " must be unloaded"));
        }

        Zip.unzipWorld(worldName);

        src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been extracted successfully"));

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getBackupNames()).keySet(),
                                key -> Finder.toMap(Finder.getBackupNames()).get(key)
                        )
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
