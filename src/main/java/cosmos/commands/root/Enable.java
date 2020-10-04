package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

public class Enable extends AbstractCommand {

    public Enable() {
        super(
                Arguments.limitCompleteElement(
                        WorldNameArguments.disabledChoices(ArgKeys.DISABLED_WORLD)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne(ArgKeys.DISABLED_WORLD.t)
                .orElseThrow(Outputs.INVALID_DISABLED_WORLD_CHOICE.asSupplier());

        WorldProperties worldProperties = FinderWorldProperties.getDisabledWorldProperties(worldName)
                .orElseThrow(Outputs.MISSING_DISABLED_WORLD.asSupplier(worldName));

        worldProperties.setEnabled(true);

        FinderWorldProperties.saveProperties(worldProperties);
        src.sendMessage(Outputs.ENABLE_WORLD.asText(worldProperties));
    }
}
