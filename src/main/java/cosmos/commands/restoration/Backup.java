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

public class Backup extends AbstractCommand {

    public Backup() {
        super(Permission.COMMAND_RESTORATION_BACKUP);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name").orElseThrow(supplyError("Please insert a valid world name"));

        if (!Finder.isAvailable(worldName)) {
            throw new CommandException(Text.of("Unable to find ", worldName, "'s folder"));
        }

        if (Sponge.getServer().getWorld(worldName).isPresent()) {
            throw new CommandException(Text.of(worldName, " must be unloaded"));
        }

        try {
            Zip.zipWorld(worldName);
        } catch (Exception e) {
            logError(e, "An error occurred while backuping ", worldName);
            throw new CommandException(Text.of("An error occurred while backuping ", worldName), e);
        }

        src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been backuped successfully"));

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getUnloadedWorldNames()).keySet(),
                                key -> Finder.toMap(Finder.getUnloadedWorldNames()).get(key)
                        )
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
