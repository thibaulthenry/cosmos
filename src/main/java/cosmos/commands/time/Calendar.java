package cosmos.commands.time;

import cosmos.constants.Outputs;
import cosmos.statics.handlers.TimeConverter;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

public class Calendar extends AbstractTimeCommand {

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) {
        long worldTime = worldProperties.getWorldTime();

        src.sendMessage(
                Outputs.CALENDAR.asText(
                        worldProperties,
                        TimeConverter.fromWorldTimeToDayWatch(worldTime),
                        TimeConverter.fromWorldTimeToDayNumber(worldTime),
                        RealTime.hasRealTimeTask(worldProperties) ? "activated" : "disabled"
                )
        );
    }
}
