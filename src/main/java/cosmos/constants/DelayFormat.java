package cosmos.constants;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.util.Ticks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum DelayFormat {

    DIGITAL_CLOCK(Ticks.single(), DataKeys.Portal.Delay.Format.DIGITAL_CLOCK),
    PERCENTAGE(Ticks.single(), DataKeys.Portal.Delay.Format.PERCENTAGE),
    SECONDS(Ticks.of(20), DataKeys.Portal.Delay.Format.SECONDS),
    TICKS(Ticks.single(), DataKeys.Portal.Delay.Format.TICKS);

    private static final Map<ResourceKey, DelayFormat> FROM_KEY = new HashMap<>();

    static {
        Arrays.stream(DelayFormat.values()).forEach(format -> FROM_KEY.putIfAbsent(format.key, format));
    }

    private final Ticks interval;
    private final ResourceKey key;

    DelayFormat(final Ticks interval, final ResourceKey key) {
        this.interval = interval;
        this.key = key;
    }

    public static Optional<DelayFormat> fromKey(final ResourceKey key) {
        return Optional.ofNullable(FROM_KEY.get(key));
    }

    public Ticks interval() {
        return this.interval;
    }

    public ResourceKey key() {
        return this.key;
    }

}
