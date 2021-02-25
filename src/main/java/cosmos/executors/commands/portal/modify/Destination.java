package cosmos.executors.commands.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.portal.PortalAll;
import cosmos.executors.parameters.impl.portal.PortalFrame;
import cosmos.executors.parameters.impl.world.WorldOnline;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

@Singleton
public class Destination extends AbstractPortalModifyCommand {

    @Inject
    public Destination(final Injector injector) {
        super(
                new PortalAll().key(CosmosKeys.PORTAL_COSMOS).build(),
                injector.getInstance(WorldOnline.class).key(CosmosKeys.WORLD_DESTINATION).build(),
                Parameter.vector3d().setKey(CosmosKeys.X_Y_Z).optional().build()
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD_DESTINATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.online", "param", CosmosKeys.WORLD_DESTINATION));

        final ServerWorld world = Sponge.getServer().getWorldManager().world(worldKey)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.missing.world", "world", worldKey));

        final ServerLocation destination = context.getOne(CosmosKeys.X_Y_Z)
                .map(world::getLocation)
                .orElse(world.getLocation(world.getProperties().spawnPosition()));

        return portal.asBuilder().destination(destination).build();
    }

}
