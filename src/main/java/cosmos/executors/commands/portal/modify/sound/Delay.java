package cosmos.executors.commands.portal.modify.sound;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

import java.time.temporal.ChronoUnit;

public class Delay extends AbstractSoundModifyCommand {

    public Delay() {
        super(CosmosParameters.Builder.DURATION_WITH_UNIT.get().durationKey(CosmosKeys.INTERVAL).optional().build());
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal, final Sound sound) {
        final long duration = context.getOne(CosmosKeys.INTERVAL).orElse(1L);
        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        return portal.asBuilder().soundDelay(sound).soundDelayInterval(Ticks.ofWallClockTime(Sponge.getServer(), duration, unit)).build();
    }

}
