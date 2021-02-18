package cosmos.executors.commands.portal.modify.particles;

import com.google.common.base.CaseFormat;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

import java.time.temporal.ChronoUnit;

@Singleton
public class SpawnInterval extends AbstractPortalModifyCommand {

    public SpawnInterval() {
        super(CosmosParameters.DURATION_WITH_UNIT.get().durationKey(CosmosKeys.INTERVAL).optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final ChronoUnit unit = context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);
        final long duration = context.one(CosmosKeys.INTERVAL)
                .map(value -> {
                    super.formattedModifiedValue = value + " " + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, unit.name());
                    return value;
                })
                .orElse(1L);

        final Ticks spawnInterval = super.serviceProvider.time().fromDurationUnitToTicks(src, duration, unit);

        if (spawnInterval.ticks() < 1) {
            throw super.serviceProvider.message().getError(src, "error.invalid.interval", "param", CosmosKeys.INTERVAL);
        }

        return portal.asBuilder().particlesSpawnInterval(spawnInterval).build();
    }

    @Override
    protected String propertyPrefix() {
        return "particles";
    }

}
