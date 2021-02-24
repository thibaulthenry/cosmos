package cosmos.executors.commands.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.portal.PortalFrame;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Nausea extends AbstractPortalModifyCommand {

    @Inject
    public Nausea() {
        super(
                new PortalFrame().key(CosmosKeys.PORTAL_FRAME_COSMOS).build(),
                Parameter.bool().setKey(CosmosKeys.STATE).build()
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final boolean nausea = context.getOne(CosmosKeys.STATE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.STATE));

        return portal.asBuilder().nausea(nausea).build();
    }

}
