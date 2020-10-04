package cosmos.commands.time;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

public class Set extends AbstractTimeChangeCommand {

    public Set() {
        super(GenericArguments.longNum(ArgKeys.WORLD_TIME.t));
    }

    @Override
    long getDesiredTime(CommandContext args, WorldProperties worldProperties) throws CommandException {
        return args.<Long>getOne(ArgKeys.WORLD_TIME.t).orElseThrow(Outputs.INVALID_WORLD_TIME.asSupplier());
    }
}
