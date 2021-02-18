package cosmos.executors.commands.portal.modify;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Optional;

@Singleton
public class Destination extends AbstractPortalModifyCommand {

    public Destination() {
        super(
                CosmosParameters.PORTAL_ALL.get().build(),
                Sponge.game().builderProvider().provide(Parameter.SequenceBuilder.class)
                        .then(CosmosParameters.WORLD_ALL.get().key(CosmosKeys.WORLD_DESTINATION).build())
                        .then(Parameter.vector3d().key(CosmosKeys.X_Y_Z).optional().build())
                        .optional()
                        .build()
        );
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Optional<ResourceKey> optionalWorldKey = context.one(CosmosKeys.WORLD_DESTINATION);

        if (!optionalWorldKey.isPresent()) {
            return portal.asBuilder().destination(null).build();
        }

        final ServerWorld world = optionalWorldKey
                .flatMap(Sponge.server().worldManager()::world)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.missing.world", "world", optionalWorldKey.get()));

        final ServerLocation destination = context.one(CosmosKeys.X_Y_Z)
                .map(world::location)
                .orElse(world.location(world.properties().spawnPosition()));

        super.formattedModifiedValue = destination;

        return portal.asBuilder().destination(destination).build();
    }

}
