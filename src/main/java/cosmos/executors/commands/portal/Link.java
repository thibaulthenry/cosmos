package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.portal.PortalType;

import java.util.Optional;

@Singleton
public class Link extends AbstractCommand {

    public Link() {
        super(
                Parameter.registryElement(TypeToken.get(PortalType.class), RegistryTypes.PORTAL_TYPE, Cosmos.NAMESPACE, ResourceKey.MINECRAFT_NAMESPACE)
                        .key(CosmosKeys.PORTAL_TYPE)
                        .build(),
                CosmosParameters.WORLD_ALL.get().key(CosmosKeys.WORLD_ORIGIN).build(),
                CosmosParameters.WORLD_ALL.get().key(CosmosKeys.WORLD_DESTINATION).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final PortalType portalType = context.one(CosmosKeys.PORTAL_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_TYPE));

        final ResourceKey worldOriginKey = context.one(CosmosKeys.WORLD_ORIGIN)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.WORLD_ORIGIN));

        final Optional<ResourceKey> optionalWorldDestinationKey = context.one(CosmosKeys.WORLD_DESTINATION);

        if (optionalWorldDestinationKey.isPresent()) {
            final ResourceKey worldDestinationKey = optionalWorldDestinationKey.get();
            super.serviceProvider.portal().link(src, worldOriginKey, portalType, worldDestinationKey);

            super.serviceProvider.message()
                    .getMessage(src, "success.portal.link")
                    .replace("destination", worldDestinationKey)
                    .replace("origin", worldOriginKey)
                    .replace("type", portalType.key(RegistryTypes.PORTAL_TYPE))
                    .green()
                    .sendTo(src);
        } else {
            super.serviceProvider.portal().unlink(src, worldOriginKey, portalType);

            super.serviceProvider.message()
                    .getMessage(src, "success.portal.unlink")
                    .replace("origin", worldOriginKey)
                    .replace("type", portalType.key(RegistryTypes.PORTAL_TYPE))
                    .green()
                    .sendTo(src);
        }
    }

}
