package cosmos.commands.management;

import cosmos.commands.AbstractCommand;
import cosmos.utils.Finder;
import cosmos.utils.Permission;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.concurrent.ExecutionException;

public class Delete extends AbstractCommand {

    public Delete() {
        super(Permission.COMMAND_MANAGEMENT_DELETE);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));
        WorldProperties worldProperties = Sponge.getServer().getWorldProperties(worldName)
                .orElseThrow(supplyError("Please insert a valid world name"));

        if (Sponge.getServer().getWorld(worldName).isPresent()) {
            throw new CommandException(Text.of(worldName, " must be unloaded before you can delete"));
        }

        try {
            Sponge.getServer().deleteWorld(worldProperties).get();
            src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been deleted successfully"));
        } catch (InterruptedException | ExecutionException e) {
            logError(e, "An error occurred while deleting ", worldName);
            throw new CommandException(Text.of("An error occurred while deleting ", worldName), e);
        }

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
