package cosmos.executors.commands.portal.modify;

import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public abstract class AbstractPortalModifyCommand extends AbstractCommand {

    protected AbstractPortalModifyCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosPortal portal = context.getOne(CosmosKeys.PORTAL_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_COSMOS));

        super.serviceProvider.registry().portal().replace(this.getNewPortal(src, context, portal));

        // todo message
    }

    protected abstract CosmosPortal getNewPortal(Audience src, CommandContext context, CosmosPortal portal) throws CommandException;

}
