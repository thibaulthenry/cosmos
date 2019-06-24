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

import java.util.Optional;

public class Rename extends AbstractCommand {

    public Rename() {
        super(Permission.COMMAND_MANAGEMENT_RENAME);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String newName = args.<String>getOne("new-name").orElseThrow(supplyError("Please insert a valid new world name"));
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid target world name"));
        WorldProperties worldProperties = Sponge.getServer().getWorldProperties(worldName)
                .orElseThrow(supplyError("Please insert a valid target world name"));

        if (Sponge.getServer().getWorld(worldName).isPresent()) {
            throw new CommandException(Text.of(worldName, " must be unloaded"));
        }

        if (Sponge.getServer().getWorldProperties(newName).isPresent()) {
            throw new CommandException(Text.of("A world already exists with ", newName, " as name"));
        }

        Optional<WorldProperties> optionalNewWorldProperties = Sponge.getServer().renameWorld(worldProperties, newName);

        if (!optionalNewWorldProperties.isPresent()) {
            throw new CommandException(Text.of("An error occurred while renaming ", worldName, " in ", newName));
        }

        src.sendMessage(Text.of(TextColors.DARK_GREEN, worldName, " has been renamed as ", newName));

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getAvailableWorldNames(true)).keySet(),
                                key -> Finder.toMap(Finder.getAvailableWorldNames(true)).get(key)
                        ),
                        GenericArguments.string(Text.of("new-name"))
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
