package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.CosmosPortalTypes;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Create extends AbstractCommand {

    public Create() {
        super(
                Parameter.string().key(CosmosKeys.NAME).build(),
                CosmosParameters.PORTAL_TYPE_COSMOS.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey portalKey = context.one(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        final CosmosPortalType portalType = context.one(CosmosKeys.PORTAL_TYPE_COSMOS).orElseGet(CosmosPortalTypes.FRAME);
        final CosmosPortal portal = super.serviceProvider.portal().create(src, portalKey, portalType);

        super.serviceProvider.message()
                .getMessage(src, "success.portal.create")
                .replace("portal", portal)
                .green()
                .sendTo(src);
    }

}
