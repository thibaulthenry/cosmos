package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.PortalRegistry;
import cosmos.services.transportation.TransportationService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;

@Singleton
public class PortalFrameListener extends AbstractListener {

    private final PortalRegistry portalRegistry;
    private final TransportationService transportationService;

    @Inject
    public PortalFrameListener(final Injector injector) {
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onMoveEntityEvent(final MoveEntityEvent event, @First final ServerPlayer player) {
        if (event.getCause().contains(Cosmos.getPluginContainer())) {
            return;
        }

        final ServerLocation eyesLocation = player.getWorld().getLocation(player.eyePosition().get());
        final ServerLocation feetLocation = player.getServerLocation();

        if (!(CosmosFramePortal.isAnyOfTriggers(eyesLocation.getBlockType()) || CosmosFramePortal.isAnyOfTriggers(feetLocation.getBlockType()))) {
            return;
        }

        this.portalRegistry.find(feetLocation.asLocatableBlock())
                .map(Optional::of)
                .orElse(this.portalRegistry.find(eyesLocation.asLocatableBlock()))
                .filter(portal -> portal instanceof CosmosFramePortal)
                .ifPresent(portal -> this.transportationService.teleport(player, portal));
    }

}
