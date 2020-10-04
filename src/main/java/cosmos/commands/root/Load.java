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
import org.spongepowered.api.world.storage.WorldProperties;

public class Load extends AbstractCommand {

    public Load() {
        super(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.unloadedChoices(ArgKeys.UNLOADED_WORLD, true)
                )
        );
    }

    private static void load(WorldProperties unloadedWorldProperties, boolean verifyLoaded) throws CommandException {
        if (verifyLoaded && Sponge.getServer().getWorld(unloadedWorldProperties.getUniqueId()).isPresent()) {
            throw Outputs.LOADING_LOADED_WORLD.asException(unloadedWorldProperties);
        }

        if (!Sponge.getServer().loadWorld(unloadedWorldProperties).isPresent()) {
            throw Outputs.LOADING_WORLD.asException(unloadedWorldProperties);
        }

        unloadedWorldProperties.setLoadOnStartup(true);

        FinderWorldProperties.saveProperties(unloadedWorldProperties);
    }

    public static void load(String worldName, boolean verifyLoaded) throws CommandException {
        WorldProperties worldProperties = Sponge.getServer().getWorldProperties(worldName)
                .orElseThrow(() -> Outputs.MISSING_WORLD.asException(worldName));

        load(worldProperties, verifyLoaded);
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties>getOne(ArgKeys.UNLOADED_WORLD.t)
                .orElseThrow(Outputs.INVALID_UNLOADED_WORLD_CHOICE.asSupplier());

        src.sendMessage(Outputs.LOAD_WORLD_START.asText(worldProperties));

        load(worldProperties, false);

        src.sendMessage(Outputs.LOAD_WORLD.asText(worldProperties));
    }
}
