package cosmos.services.time.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Units;
import cosmos.services.message.MessageService;
import cosmos.services.time.TimeService;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.util.Ticks;

import java.time.temporal.ChronoUnit;

@Singleton
public class TimeServiceImpl implements TimeService {

    private final MessageService messageService;

    @Inject
    public TimeServiceImpl(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public Ticks fromDurationUnitToTicks(final Audience src, final long duration, final ChronoUnit unit) throws CommandException {
        try {
            return Ticks.ofWallClockTime(Sponge.server(), duration, unit);
        } catch (final Exception ignored) {
            throw this.messageService.getError(src, "error.result.nan");
        }
    }

    @Override
    public int fromSecondsToTicks(final int seconds) {
        return seconds * Units.TICKS_PER_SECONDS;
    }

    @Override
    public int fromTicksToSeconds(final long ticks) {
        return (int) (ticks / Units.TICKS_PER_SECONDS);
    }

    @Override
    public int fromWorldTimeToDayNumber(long worldTime) {
        worldTime += Units.MIDNIGHT_OFFSET_IN_TICKS;
        return (int) worldTime / Units.DAY_DURATION_IN_TICKS;
    }

    @Override
    public String fromWorldTimeToDayWatch(long worldTime) {
        worldTime += Units.MIDNIGHT_OFFSET_IN_TICKS;
        final int ticksOfDay = (int) (worldTime % Units.DAY_DURATION_IN_TICKS);
        final int secondsOfDay = ticksOfDay * Units.DAY_DURATION_IN_SECONDS / Units.DAY_DURATION_IN_TICKS;
        final int hourOfDay = secondsOfDay / Units.HOUR_DURATION_IN_SECONDS;
        final int minutesOfHour = (secondsOfDay % Units.HOUR_DURATION_IN_SECONDS) / Units.MINUTE_DURATION_IN_SECONDS;
        final int secondsOfMinute = secondsOfDay % Units.MINUTE_DURATION_IN_SECONDS;
        return String.format("%02d:%02d:%02d", hourOfDay, minutesOfHour, secondsOfMinute);
    }

    @Override
    public long getFutureTickDifference(long worldTime, long desiredTick, final boolean incrementDayOnZero) {
        desiredTick = Math.min(Math.max(desiredTick, 0), Units.DAY_DURATION_IN_TICKS);

        worldTime %= Units.DAY_DURATION_IN_TICKS;
        final long futureTick = desiredTick - worldTime;

        return incrementDayOnZero && futureTick == 0 || futureTick < 0 ? futureTick + Units.DAY_DURATION_IN_TICKS : futureTick;
    }

    @Override
    public long getNearestTickOccurrence(final long worldTime, long desiredTick) {
        desiredTick = Math.min(Math.max(desiredTick, 0), Units.DAY_DURATION_IN_TICKS);

        if (worldTime % Units.DAY_DURATION_IN_TICKS == desiredTick) {
            return worldTime;
        }

        final long pastDifference = getPastTickDifference(worldTime, desiredTick);
        final long futureDifference = getFutureTickDifference(worldTime, desiredTick, false);
        final long pastTickOccurrence = worldTime + pastDifference;
        final long futureTickOccurrence = worldTime + futureDifference;

        if (pastTickOccurrence < 0) {
            return futureTickOccurrence;
        }

        return Math.abs(pastDifference) <= futureDifference ? pastTickOccurrence : futureTickOccurrence;
    }

    @Override
    public long getPastMidnight(final long worldTime) {
        return worldTime - Math.abs(getPastTickDifference(worldTime, Units.MIDNIGHT));
    }

    private long getPastTickDifference(long worldTime, long desiredTick) {
        desiredTick = Math.min(Math.max(desiredTick, 0), Units.DAY_DURATION_IN_TICKS);

        worldTime %= Units.DAY_DURATION_IN_TICKS;
        final long pastTick = desiredTick - worldTime;

        return pastTick <= 0 ? pastTick : pastTick - Units.DAY_DURATION_IN_TICKS;
    }

}
