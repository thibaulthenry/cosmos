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
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;

@Singleton
public class PortalProtectionListener extends AbstractListener {

    private final PortalRegistry portalRegistry;

    @Inject
    public PortalProtectionListener(final Injector injector) {
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
    }

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onAllChangeBlockEvent(final ChangeBlockEvent.All event) {
        boolean usingDebugStick = event.getCause().first(ServerPlayer.class)
                .map(player -> player.getItemInHand(HandTypes.MAIN_HAND))
                .filter(itemStack -> itemStack.getType().isAnyOf(ItemTypes.DEBUG_STICK.get()))
                .isPresent();

        if (event.getCause().contains(Cosmos.getPluginContainer()) || usingDebugStick) {
            return;
        }

        final boolean affectPortal = event.getTransactions()
                .stream()
                .map(Transaction::getOriginal)
                .filter(blockSnapshot -> blockSnapshot.getLocation().isPresent())
                .map(blockSnapshot -> LocatableBlock.builder()
                        .location(blockSnapshot.getLocation().get())
                        .state(blockSnapshot.getState())
                        .build()
                )
                .anyMatch(locatableBlock -> this.portalRegistry.find(locatableBlock).isPresent());

        event.setCancelled(affectPortal);
    }

//    @Listener todo
//    @IsCancelled(value = Tristate.FALSE)
//    public void onNotifyNeighborBlockEvent(final NotifyNeighborBlockEvent event) {
//        if (event.getCause().contains(Cosmos.getPluginContainer())) {
//            return;
//        }
//
//        final Optional<LocatableBlock> optionalNotifierBlock = event.getCause().first(LocatableBlock.class);
//
//        if (!optionalNotifierBlock.isPresent()) {
//            return;
//        }
//
//        final LocatableBlock notifierBlock = optionalNotifierBlock.get();
//
//        event.filterDirections(direction -> {
//            final LocatableBlock locatableBlock = notifierBlock.getServerLocation()
//                    .add(direction.asBlockOffset())
//                    .asLocatableBlock();
//
//            return !this.portalFrameRegistry.find(locatableBlock).isPresent();
//        });
//    }

}
