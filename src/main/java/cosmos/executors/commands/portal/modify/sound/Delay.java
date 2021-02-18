package cosmos.executors.commands.portal.modify.sound;

import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.formatter.impl.SoundFormatter;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

import java.time.temporal.ChronoUnit;

public class Delay extends AbstractSoundModifyCommand {

    public Delay() {
        super(CosmosParameters.DURATION_WITH_UNIT.get().durationKey(CosmosKeys.INTERVAL).optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal, final Sound sound) throws CommandException {
        final long duration = context.one(CosmosKeys.INTERVAL).orElse(1L);
        final ChronoUnit unit = context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        final Ticks delayInterval = super.serviceProvider.time().fromDurationUnitToTicks(src, duration, unit);

        if (delayInterval.ticks() < 1) {
            throw super.serviceProvider.message().getError(src, "error.invalid.interval", "param", CosmosKeys.INTERVAL);
        }

        super.formattedModifiedValue = ((SoundFormatter) super.serviceProvider.registry().formatter().value(Sound.class))
                .asText(sound, delayInterval, super.serviceProvider.message().getLocale(src));

        return portal.asBuilder()
                .soundDelay(sound)
                .soundDelayInterval(delayInterval)
                .build();
    }

}
