package cosmos.executors.commands.time;

import com.google.inject.Singleton;
import cosmos.constants.Units;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.MinecraftDayTime;

@Singleton
public class Midday extends AbstractTimeModifyCommand {

    @Override
    protected long newTime(final Audience src, final CommandContext context, final MinecraftDayTime time) {
        return super.serviceProvider.time().getNearestTickOccurrence(time.asTicks().ticks(), Units.MIDDAY);
    }

}
