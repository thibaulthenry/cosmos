package cosmos.commands.properties;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.world.storage.WorldProperties;

public abstract class AbstractPropertyCommand extends AbstractCommand {

    private WorldProperties worldProperties;

    protected AbstractPropertyCommand(CommandElement... commandElements) {
        super(commandElements);
    }

    protected WorldProperties getWorldProperties() {
        return worldProperties;
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        worldProperties = FinderWorldProperties.getGivenWorldPropertiesOrElse(src, args, ArgKeys.WORLD)
                .orElseThrow(Outputs.INVALID_WORLD_NAME.asSupplier());

        runWithProperties(src, args, worldProperties);
    }

    protected abstract void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException;
}
