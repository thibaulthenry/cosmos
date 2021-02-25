package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.CosmosButtonPortal;
import cosmos.registries.portal.PortalRegistry;
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
public class PortalButtonListener extends AbstractListener {

    private final PortalRegistry portalRegistry;
    private final TransportationService transportationService;

    @Inject
    public PortalButtonListener(final Injector injector) {
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onInteractBlockEvent(final InteractBlockEvent.Secondary event, @First final ServerPlayer player) {
        if (event.getCause().contains(Cosmos.getPluginContainer())
                || !(CosmosButtonPortal.isAnyOfTriggers(event.getBlock().getState().getType()))
                || ItemTypes.DEBUG_STICK.get().isAnyOf(player.getItemInHand(HandTypes.MAIN_HAND.get()).getType())) {
            return;
        }

        event.getBlock().getLocation()
                .map(ServerLocation::asLocatableBlock)
                .flatMap(this.portalRegistry::find)
                .filter(portal -> portal instanceof CosmosButtonPortal)
                .ifPresent(portal -> this.transportationService.teleport(player, portal));
    }

}
