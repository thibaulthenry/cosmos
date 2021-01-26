package cosmos.executors.commands.time;

import com.google.inject.Singleton;
import cosmos.constants.Units;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.MinecraftDayTime;

@Singleton
public class Dusk extends AbstractTimeChangeCommand {

    @Override
    protected long getNewTime(final Audience src, final CommandContext context, final MinecraftDayTime time) {
        return super.serviceProvider.time().getNearestTickOccurrence(time.asTicks().getTicks(), Units.DUSK);
    }

}
