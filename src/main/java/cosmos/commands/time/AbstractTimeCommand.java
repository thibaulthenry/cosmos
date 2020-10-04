package cosmos.commands.time;

import cosmos.commands.properties.AbstractPropertyCommand;
import cosmos.constants.Units;
import org.spongepowered.api.command.args.CommandElement;

abstract class AbstractTimeCommand extends AbstractPropertyCommand {

    AbstractTimeCommand(CommandElement... commandElements) {
        super(commandElements);
    }

    static long getNearestDawn(long worldTime) {
        return getNearestTickOccurrence(worldTime, Units.DAWN);
    }

    static long getNearestMidday(long worldTime) {
        return getNearestTickOccurrence(worldTime, Units.MIDDAY);
    }

    static long getNearestDusk(long worldTime) {
        return getNearestTickOccurrence(worldTime, Units.DUSK);
    }

    static long getNearestMidnight(long worldTime) {
        return getNearestTickOccurrence(worldTime, Units.MIDNIGHT);
    }

    static long getPastMidnight(long worldTime) {
        return worldTime - Math.abs(getPastTickDifference(worldTime, Units.MIDNIGHT));
    }

    static long getFutureTickDifference(long worldTime, long desiredTick, boolean incrementDayOnZero) {
        desiredTick = Math.min(Math.max(desiredTick, 0), Units.DAY_DURATION_IN_TICKS);

        worldTime %= Units.DAY_DURATION_IN_TICKS;
        long futureTick = desiredTick - worldTime;

        return incrementDayOnZero && futureTick == 0 || futureTick < 0 ? futureTick + Units.DAY_DURATION_IN_TICKS : futureTick;
    }

    private static long getPastTickDifference(long worldTime, long desiredTick) {
        desiredTick = Math.min(Math.max(desiredTick, 0), Units.DAY_DURATION_IN_TICKS);

        worldTime %= Units.DAY_DURATION_IN_TICKS;
        long pastTick = desiredTick - worldTime;

        return pastTick <= 0 ? pastTick : pastTick - Units.DAY_DURATION_IN_TICKS;
    }

    private static long getNearestTickOccurrence(long worldTime, long desiredTick) {
        desiredTick = Math.min(Math.max(desiredTick, 0), Units.DAY_DURATION_IN_TICKS);

        if (worldTime % Units.DAY_DURATION_IN_TICKS == desiredTick) {
            return worldTime;
        }

        long pastDifference = getPastTickDifference(worldTime, desiredTick);
        long futureDifference = getFutureTickDifference(worldTime, desiredTick, false);
        long pastTickOccurrence = worldTime + pastDifference;
        long futureTickOccurrence = worldTime + futureDifference;

        if (pastTickOccurrence < 0) return futureTickOccurrence;
        return Math.abs(pastDifference) <= futureDifference ? pastTickOccurrence : futureTickOccurrence;
    }

}
