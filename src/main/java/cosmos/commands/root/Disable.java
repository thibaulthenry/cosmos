package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldPropertiesArguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

public class Disable extends AbstractCommand {

    public Disable() {
        super(
                Arguments.limitCompleteElement(
                        WorldPropertiesArguments.unloadedChoices(ArgKeys.UNLOADED_WORLD, false)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties>getOne(ArgKeys.UNLOADED_WORLD.t)
                .orElseThrow(Outputs.INVALID_UNLOADED_WORLD_CHOICE.asSupplier());

        worldProperties.setEnabled(false);


        FinderWorldProperties.saveProperties(worldProperties);
        src.sendMessage(Outputs.DISABLE_WORLD.asText(worldProperties));
    }

}
