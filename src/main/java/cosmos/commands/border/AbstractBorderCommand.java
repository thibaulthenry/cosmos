package cosmos.commands.border;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorld;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

abstract class AbstractBorderCommand extends AbstractCommand {

    AbstractBorderCommand(CommandElement... commandElements) {
        super(commandElements);
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        World world = FinderWorld.getGivenWorldOrElse(src, args, ArgKeys.LOADED_WORLD)
                .orElseThrow(Outputs.INVALID_LOADED_WORLD_CHOICE.asSupplier());


        runWithBorder(src, args, world.getProperties(), world.getWorldBorder());
    }

    abstract void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException;
}
