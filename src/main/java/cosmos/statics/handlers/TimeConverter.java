package cosmos.statics.handlers;

import cosmos.constants.Units;

public class TimeConverter {

    public static int fromSecondsToTicks(int seconds) {
        return seconds * Units.TICKS_PER_SECONDS;
    }

    public static int fromTicksToSeconds(long ticks) {
        return (int) (ticks / Units.TICKS_PER_SECONDS);
    }

    public static int fromWorldTimeToDayNumber(long worldTime) {
        worldTime += Units.MIDNIGHT_OFFSET_IN_TICKS;
        return (int) worldTime / Units.DAY_DURATION_IN_TICKS;
    }

    public static String fromWorldTimeToDayWatch(long worldTime) {
        worldTime += Units.MIDNIGHT_OFFSET_IN_TICKS;
        int ticksOfDay = (int) (worldTime % Units.DAY_DURATION_IN_TICKS);
        int secondsOfDay = ticksOfDay * Units.DAY_DURATION_IN_SECONDS / Units.DAY_DURATION_IN_TICKS;
        int hourOfDay = secondsOfDay / Units.HOUR_DURATION_IN_SECONDS;
        int minutesOfHour = (secondsOfDay % Units.HOUR_DURATION_IN_SECONDS) / Units.MINUTE_DURATION_IN_SECONDS;
        int secondsOfMinute = secondsOfDay % Units.MINUTE_DURATION_IN_SECONDS;
        return String.format("%02d:%02d:%02d", hourOfDay, minutesOfHour, secondsOfMinute);
    }

}
