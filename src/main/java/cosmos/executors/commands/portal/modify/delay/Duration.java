package cosmos.executors.commands.portal.modify.delay;

import com.google.common.base.CaseFormat;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Singleton
public class Duration extends AbstractDelayModifyCommand {

    public Duration() {
        super(CosmosParameters.DURATION_WITH_UNIT.get().optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Optional<Long> optionalDuration = context.one(CosmosKeys.DURATION);

        if (!optionalDuration.isPresent()) {
            return portal.asBuilder().delay(null).build();
        }

        final long duration = optionalDuration.get();
        final ChronoUnit unit = context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);
        super.formattedModifiedValue = duration + " " + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, unit.name());

        return portal.asBuilder().delay(super.serviceProvider.time().fromDurationUnitToTicks(src, duration, unit)).build();
    }

}
