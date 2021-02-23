package cosmos.executors.commands.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.portal.PortalFrame;
import cosmos.registries.portal.CosmosFramePortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

import java.time.temporal.ChronoUnit;

@Singleton
public class Cooldown extends AbstractPortalModifyCommand { // todo rename to delay

    @Inject
    public Cooldown() {
        super(
                new PortalFrame().key(CosmosKeys.PORTAL_FRAME_COSMOS).build(),
                CosmosParameters.DURATION_WITH_TIME_UNIT
        );
    }

    @Override
    protected CosmosFramePortal getNewPortal(final Audience src, final CommandContext context, final CosmosFramePortal portal) throws CommandException {
        final long duration = context.getOne(CosmosKeys.DURATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.DURATION));

        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        return portal.asBuilder().cooldown(Ticks.zero()).build(); // todo
    }

}
