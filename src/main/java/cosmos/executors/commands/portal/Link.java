package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.portal.PortalType;

@Singleton
public class Link extends AbstractCommand {

    public Link() {
        super(
                Parameter.registryElement(TypeToken.get(PortalType.class), RegistryTypes.PORTAL_TYPE)
                        .setKey(CosmosKeys.PORTAL_TYPE)
                        .build(),
                CosmosParameters.Builder.WORLD_ALL.get().key(CosmosKeys.WORLD_ORIGIN).optional().build(),
                CosmosParameters.Builder.WORLD_ALL.get().key(CosmosKeys.WORLD_DESTINATION).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final PortalType portalType = context.getOne(CosmosKeys.PORTAL_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_TYPE));

        final ResourceKey worldOriginKey = super.serviceProvider.world().getKeyOrSource(context, CosmosKeys.WORLD_ORIGIN);

        final ResourceKey worldDestinationKey = context.getOne(CosmosKeys.WORLD_DESTINATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.WORLD_DESTINATION));


        super.serviceProvider.portal().link(worldOriginKey, portalType, worldDestinationKey);

        // todo
    }

}
