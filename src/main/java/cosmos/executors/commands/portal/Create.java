package cosmos.executors.commands.portal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosPortalTypes;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.portal.PortalTypeCosmos;
import cosmos.registries.data.portal.CosmosPortalType;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Create extends AbstractCommand {

    @Inject
    public Create() {
        super(
                Parameter.string().setKey(CosmosKeys.NAME).build(),
                new PortalTypeCosmos().key(CosmosKeys.PORTAL_TYPE_COSMOS).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey newKey = context.getOne(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        final CosmosPortalType portalType = context.getOne(CosmosKeys.PORTAL_TYPE_COSMOS).orElseGet(CosmosPortalTypes.FRAME);

        super.serviceProvider.portal().create(src, newKey, portalType); // todo

        // todo
    }

}
