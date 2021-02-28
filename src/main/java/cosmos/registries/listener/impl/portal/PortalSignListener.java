package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.CosmosButtonPortal;
import cosmos.registries.portal.CosmosSignPortal;
import cosmos.registries.portal.PortalRegistry;
import cosmos.registries.portal.PortalTeleportTaskRegistry;
import cosmos.services.transportation.TransportationService;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.server.ServerLocation;

@Singleton
public class PortalSignListener extends AbstractListener {

    private final PortalRegistry portalRegistry;
    private final PortalTeleportTaskRegistry portalTeleportTaskRegistry;
    private final TransportationService transportationService;

    @Inject
    public PortalSignListener(final Injector injector) {
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.portalTeleportTaskRegistry = injector.getInstance(PortalTeleportTaskRegistry.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onInteractBlockEvent(final InteractBlockEvent.Secondary event, @First final ServerPlayer player) {
        if (event.getCause().contains(Cosmos.getPluginContainer())
                || !(CosmosSignPortal.isAnyOfTriggers(event.getBlock().getState().getType()))
                || ItemTypes.DEBUG_STICK.get().isAnyOf(player.getItemInHand(HandTypes.MAIN_HAND.get()).getType())
                || this.portalTeleportTaskRegistry.has(player.getUniqueId())) {
            return;
        }

        event.getBlock().getLocation()
                .map(ServerLocation::asLocatableBlock)
                .flatMap(this.portalRegistry::find)
                .filter(portal -> portal instanceof CosmosSignPortal)
                .ifPresent(portal -> {
                    event.setCancelled(true);
                    this.transportationService.teleport(player, portal);
                });
    }

}
