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
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class Load extends AbstractCommand {

    public Load() {
        super(Permission.COMMAND_MANAGEMENT_LOAD);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));
        WorldProperties worldProperties = Sponge.getServer().getWorldProperties(worldName)
                .orElseThrow(supplyError("Please insert a valid world name"));

        if (Sponge.getServer().getWorld(worldProperties.getUniqueId()).isPresent()) {
            throw new CommandException(Text.of(worldName, " is already loaded"));
        }

        if (!Finder.isAvailable(worldName)) {
            throw new CommandException(Text.of("Unable to find ", worldName, "'s level.dat file"));
        }

        src.sendMessage(Text.of(TextColors.DARK_GRAY, worldName, " about to be loaded.."));
        worldProperties.setEnabled(true);

        Optional<World> world = Sponge.getServer().loadWorld(worldProperties);

        if (!world.isPresent()) {
            throw new CommandException(Text.of("An error occurred while loading ", worldName));
        }

        worldProperties.setLoadOnStartup(true);
        Sponge.getServer().saveWorldProperties(worldProperties);

        src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been loaded successfully"));

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
