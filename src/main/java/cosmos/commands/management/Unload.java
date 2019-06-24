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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;

public class Unload extends AbstractCommand {

    public Unload() {
        super(Permission.COMMAND_MANAGEMENT_UNLOAD);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));
        WorldProperties worldProperties = Sponge.getServer().getWorldProperties(worldName)
                .orElseThrow(supplyError("Please insert a valid world name"));

        if (Sponge.getServer().getDefaultWorldName().equals(worldName)) {
            throw new CommandException(Text.of("Default world cannot be unloaded"));
        }

        World defaultWorld = Sponge.getServer().getWorld(Sponge.getServer().getDefaultWorldName()).orElseThrow(() -> new CommandException(Text.of("An error occurred while catching the default world")));
        World world = Sponge.getServer().getWorld(worldProperties.getUniqueId())
                .orElseThrow(() -> new CommandException(Text.of(worldName, " isn't loaded")));

        world.getEntities().stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .forEach(player -> player.setLocation(defaultWorld.getSpawnLocation()));

        worldProperties.setLoadOnStartup(false);
        worldProperties.setEnabled(false);
        Sponge.getServer().saveWorldProperties(worldProperties);

        if (!Sponge.getServer().unloadWorld(world)) {
            throw new CommandException(Text.of("An error occurred while unloading ", worldName));
        }

        boolean worldSaved;

        try {
            worldSaved = world.save();
        } catch (IOException e) {
            logError(e, "An error occurred while saving ", worldName, " after unloading");
            throw new CommandException(Text.of("An error occurred while saving ", worldName, " after unloading"), e);
        }

        if (!worldSaved) {
            throw new CommandException(Text.of("An error occurred while saving ", worldName, " after unloading"));
        }

        src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been unloaded successfully"));

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getLoadedWorldNames()).keySet(),
                                key -> Finder.toMap(Finder.getLoadedWorldNames()).get(key)
                        )
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
