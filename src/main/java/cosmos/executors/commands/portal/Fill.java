package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Fill extends AbstractCommand {

    public Fill() {
        super(CosmosParameters.Builder.PORTAL_ALL.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosPortal portal = context.getOne(CosmosKeys.PORTAL_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_COSMOS));

        super.serviceProvider.portal().fill(src, portal);

        // todo
    }

}
