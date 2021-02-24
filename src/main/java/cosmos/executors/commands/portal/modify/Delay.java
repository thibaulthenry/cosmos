package cosmos.executors.commands.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.portal.PortalFrame;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

@Singleton
public class Delay extends AbstractPortalModifyCommand {

    @Inject
    public Delay() {
        super(
                new PortalFrame().key(CosmosKeys.PORTAL_FRAME_COSMOS).build(),
                CosmosParameters.DURATION_WITH_TIME_UNIT
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final long duration = context.getOne(CosmosKeys.DURATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.DURATION));

        return portal.asBuilder().delay(Ticks.zero()).build(); // todo
    }

}
