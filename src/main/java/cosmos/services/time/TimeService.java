package cosmos.services.time;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.time.impl.TimeServiceImpl;

@ImplementedBy(TimeServiceImpl.class)
public interface TimeService extends CosmosService {

    long getNearestTickOccurrence(final long worldTime, long desiredTick);

    long getPastMidnight(long worldTime);

    long getFutureTickDifference(long worldTime, long desiredTick, boolean incrementDayOnZero);

    int fromSecondsToTicks(int seconds);

    int fromTicksToSeconds(long ticks);

    int fromWorldTimeToDayNumber(long worldTime);

    String fromWorldTimeToDayWatch(long worldTime);

}
