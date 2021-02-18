package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosPortalTypes;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.CosmosButtonPortal;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.PortalRegistry;
import cosmos.registries.portal.PortalTeleportTaskRegistry;
import cosmos.registries.portal.impl.PortalTeleportTask;
import cosmos.services.transportation.TransportationService;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;

@Singleton
public class PortalTeleportListener extends AbstractListener {

    private final PortalRegistry portalRegistry;
    private final PortalTeleportTaskRegistry portalTeleportTaskRegistry;
    private final TransportationService transportationService;

    @Inject
    public PortalTeleportListener(final Injector injector) {
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.portalTeleportTaskRegistry = injector.getInstance(PortalTeleportTaskRegistry.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    private void cancelTaskIfExist(final ServerPlayer player) {
        Cosmos.services().registry().portalTeleportTask()
                .find(player.uniqueId())
                .filter(task -> task.isType(CosmosPortalTypes.FRAME.get()))
                .ifPresent(PortalTeleportTask::cancel);
    }

    @Listener
    public void onMoveEntityEvent(final MoveEntityEvent event, @First final ServerPlayer player) {
        if (event.cause().contains(Cosmos.pluginContainer())) {
            return;
        }

        final ServerLocation eyesLocation = player.world().location(player.eyePosition().get());
        final ServerLocation feetLocation = player.serverLocation();

        if (!(CosmosFramePortal.isAnyOfTriggers(eyesLocation.blockType()) || CosmosFramePortal.isAnyOfTriggers(feetLocation.blockType()))) {
            this.cancelTaskIfExist(player);
            return;
        }

        final Optional<CosmosPortal> optionalPortal = this.portalRegistry.find(feetLocation.asLocatableBlock())
                .map(Optional::of)
                .orElse(this.portalRegistry.find(eyesLocation.asLocatableBlock()))
                .filter(portal -> portal instanceof CosmosFramePortal);

        if (optionalPortal.isPresent()) {
            this.transportationService.teleport(player, optionalPortal.get());
        } else {
            this.cancelTaskIfExist(player);
        }
    }

    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onSecondaryInteractBlockEvent(final InteractBlockEvent.Secondary event, @First final ServerPlayer player) {
        if (event.cause().contains(Cosmos.pluginContainer())
                || !(CosmosButtonPortal.isAnyOfTriggers(event.block().state().type()))
                || ItemTypes.DEBUG_STICK.get().isAnyOf(player.itemInHand(HandTypes.MAIN_HAND.get()).type())
                || this.portalTeleportTaskRegistry.has(player.uniqueId())) {
            return;
        }

        event.block().location()
                .map(ServerLocation::asLocatableBlock)
                .flatMap(this.portalRegistry::find)
                .ifPresent(portal -> {
                    event.setCancelled(true);
                    this.transportationService.teleport(player, portal);
                });
    }

}
