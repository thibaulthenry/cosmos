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
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

public class Rename extends AbstractCommand {

    public Rename() {
        super(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.unloadedChoices(ArgKeys.UNLOADED_WORLD, true)
                ),
                GenericArguments.string(ArgKeys.NEW_NAME.t)
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties>getOne(ArgKeys.UNLOADED_WORLD.t)
                .orElseThrow(Outputs.INVALID_UNLOADED_WORLD_CHOICE.asSupplier());
        String newName = args.<String>getOne(ArgKeys.NEW_NAME.t).orElseThrow(Outputs.INVALID_WORLD_NAME.asSupplier());

        if (Sponge.getServer().getWorldProperties(newName).isPresent()) {
            throw Outputs.EXISTING_WORLD.asException(newName);
        }

        WorldProperties newWorldProperties = Sponge.getServer().renameWorld(worldProperties, newName)
                .orElseThrow(Outputs.RENAMING_WORLD.asSupplier(worldProperties, newName));

        FinderWorldProperties.saveProperties(newWorldProperties);
        src.sendMessage(Outputs.RENAME_WORLD.asText(worldProperties, newWorldProperties));
    }
}
