package cosmos.services.time;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.time.impl.TimeServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.util.Ticks;

import java.time.temporal.ChronoUnit;

@ImplementedBy(TimeServiceImpl.class)
public interface TimeService extends CosmosService {

    Ticks fromDurationUnitToTicks(Audience src, long duration, ChronoUnit unit) throws CommandException;

    int fromSecondsToTicks(int seconds);

    int fromTicksToSeconds(long ticks);

    int fromWorldTimeToDayNumber(long worldTime);

    String fromWorldTimeToDayWatch(long worldTime);

    long getFutureTickDifference(long worldTime, long desiredTick, boolean incrementDayOnZero);

    long getNearestTickOccurrence(long worldTime, long desiredTick);

    long getPastMidnight(long worldTime);

}
