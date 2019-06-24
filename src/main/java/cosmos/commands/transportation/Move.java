package cosmos.commands.transportation;

import com.flowpowered.math.vector.Vector3d;
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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

public class Move extends AbstractCommand {

    public Move() {
        super(Permission.COMMAND_TRANSPORTATION_MOVE);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));
        WorldProperties worldProperties = Sponge.getServer().getWorldProperties(worldName)
                .orElseThrow(supplyError("Please insert a valid world name"));
        World world = Sponge.getServer().getWorld(worldProperties.getUniqueId()).orElseThrow(supplyError("Please insert a valid world name"));

        Player player = args.<Player>getOne("player").orElseThrow(supplyError("Please insert a valid player name"));

        if (!args.<Double>getOne("x").isPresent()) {
            Location<World> spawnLocation = world.getSpawnLocation();
            player.setLocation(spawnLocation);
            return CommandResult.success();
        }

        double x = args.<Double>getOne("x").get();
        double y = args.<Double>getOne("y").orElseThrow(() -> new CommandException(Text.of("Missing <y> & <z> coordinates")));
        double z = args.<Double>getOne("z").orElseThrow(() -> new CommandException(Text.of("Missing <z> coordinates")));

        Location<World> location = world.getLocation(x, y, z);
        player.setLocationSafely(location.add(0.5, 0, 0.5));

        double rx = args.<Double>getOne("rx").orElse(player.getRotation().getX());
        double ry = args.<Double>getOne("ry").orElse(player.getRotation().getY());
        double rz = args.<Double>getOne("rz").orElse(player.getRotation().getZ());

        player.setRotation(new Vector3d(rx, ry, rz));

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getLoadedWorldNames()).keySet(),
                                key -> Finder.toMap(Finder.getLoadedWorldNames()).get(key)
                        ),
                        GenericArguments.playerOrSource(Text.of("player")),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("x"))),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("y"))),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("z"))),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("rx"))),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("ry"))),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("rz")))
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
