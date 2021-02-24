package cosmos.executors.commands.portal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.portal.PortalFrame;
import cosmos.registries.portal.CosmosFramePortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Delete extends AbstractCommand {

    @Inject
    public Delete() {
        super(new PortalFrame().key(CosmosKeys.PORTAL_FRAME_COSMOS).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosFramePortal portal = context.getOne(CosmosKeys.PORTAL_FRAME_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_FRAME_COSMOS));

        super.serviceProvider.portal().delete(src, portal.key());

        // todo
    }

}
