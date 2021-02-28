package cosmos.executors.commands.portal.modify;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

import java.time.temporal.ChronoUnit;

@Singleton
public class Delay extends AbstractPortalModifyCommand {

    public Delay() {
        super(
                CosmosParameters.Builder.PORTAL_ALL.get().build(),
                CosmosParameters.Builder.DURATION_WITH_UNIT.get().build()
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final long duration = context.getOne(CosmosKeys.DURATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.DURATION));

        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        return portal.asBuilder().delay(Ticks.ofWallClockTime(Sponge.getServer(), duration, unit)).build();
    }

}
