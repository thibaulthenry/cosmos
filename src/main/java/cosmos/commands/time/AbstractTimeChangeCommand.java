package cosmos.commands.time;

import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import cosmos.statics.handlers.TimeConverter;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.world.storage.WorldProperties;

abstract class AbstractTimeChangeCommand extends AbstractTimeCommand {

    AbstractTimeChangeCommand(CommandElement... commandElements) {
        super(commandElements);
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        long time = Math.max(0, getDesiredTime(args, worldProperties));
        worldProperties.setWorldTime(time);
        FinderWorldProperties.saveProperties(worldProperties);

        src.sendMessage(
                Outputs.SET_TIME.asText(
                        worldProperties,
                        time,
                        TimeConverter.fromWorldTimeToDayNumber(time),
                        TimeConverter.fromWorldTimeToDayWatch(time)
                )
        );
    }

    abstract long getDesiredTime(CommandContext args, WorldProperties worldProperties) throws CommandException;
}
