package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldPropertiesArguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.function.Function;

public class Unload extends AbstractCommand {

    public Unload() {
        super(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.loadedChoices(ArgKeys.WORLD)
                )
        );
    }

    public static void unload(WorldProperties worldProperties, boolean verifyLoaded) throws CommandException {
        if (verifyLoaded && !FinderWorldProperties.isLoaded(worldProperties)) {
            throw Outputs.UNLOADING_UNLOADED_WORLD.asException(worldProperties);
        }

        String defaultWorldName = Sponge.getServer().getDefaultWorldName();

        if (defaultWorldName.equals(worldProperties.getWorldName())) {
            throw Outputs.UNLOADING_DEFAULT_WORLD.asException(worldProperties);
        }

        World defaultWorld = Sponge.getServer().getWorld(defaultWorldName)
                .orElseThrow(Outputs.MISSING_DEFAULT_WORLD.asSupplier(defaultWorldName));
        World world = Sponge.getServer().getWorld(worldProperties.getUniqueId())
                .orElseThrow(Outputs.MISSING_LOADED_WORLD.asSupplier(worldProperties));

        Location<World> defaultSpawnLocation = defaultWorld.getSpawnLocation();

        Function<String, Text> successFunction = (mutableText) -> Outputs.TRANSFER_TO_LOCATION.asText(
                "You", "have", mutableText,
                defaultWorldName, defaultSpawnLocation.getPosition()
        );

        Function<String, Text> errorFunction = (mutableText) -> Outputs.TRANSFERRING_TO_LOCATION.asText(
                "You", mutableText, defaultWorld.getName(), defaultSpawnLocation.getPosition()
        );

        world.getPlayers().forEach(player -> Move.move(player, defaultSpawnLocation, player.getRotation(), successFunction, errorFunction, false));

        if (!Sponge.getServer().unloadWorld(world)) {
            throw Outputs.UNLOADING_WORLD.asException(worldProperties);
        }

        worldProperties.setLoadOnStartup(false);
        FinderWorldProperties.saveProperties(worldProperties);
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties>getOne(ArgKeys.WORLD.t)
                .orElseThrow(Outputs.INVALID_LOADED_WORLD_CHOICE.asSupplier());

        unload(worldProperties, false);

        src.sendMessage(Outputs.UNLOAD_WORLD.asText(worldProperties));
    }
}
