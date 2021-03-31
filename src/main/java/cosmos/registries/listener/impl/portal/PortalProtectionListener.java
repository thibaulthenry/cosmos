package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.PortalRegistry;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.NotifyNeighborBlockEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.item.ItemTypes;
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
    @IsCancelled(Tristate.FALSE)
    public void onAllChangeBlockEvent(final ChangeBlockEvent.All event) {
        final boolean usingDebugStick = event.cause().first(ServerPlayer.class)
                .map(player -> player.itemInHand(HandTypes.MAIN_HAND))
                .filter(itemStack -> itemStack.type().isAnyOf(ItemTypes.DEBUG_STICK.get()))
                .isPresent();

        if (event.cause().contains(Cosmos.pluginContainer()) || usingDebugStick) {
            return;
        }

        final boolean affectPortal = event.transactions()
                .stream()
                .map(Transaction::original)
                .anyMatch(blockSnapshot -> {
                    final Optional<ServerLocation> optionalLocation = blockSnapshot.location();

                    if (!optionalLocation.isPresent()) {
                        return false;
                    }

                    final LocatableBlock locatableBlock = LocatableBlock.builder()
                            .location(optionalLocation.get())
                            .state(blockSnapshot.state())
                            .build();

                    return this.portalRegistry.find(locatableBlock).isPresent();
                });

        event.setCancelled(affectPortal);
    }

    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onNotifyNeighborBlockEvent(final NotifyNeighborBlockEvent event) {
        if (event.cause().contains(Cosmos.pluginContainer())) {
            return;
        }

        // TODO Does not work. Follow gabizou and Morph progress

        final boolean affectPortal = event.tickets()
                .stream()
                .anyMatch(ticket -> this.portalRegistry.find(ticket.notifier()).isPresent() ||
                        ticket.target().location()
                                .map(ServerLocation::asLocatableBlock)
                                .flatMap(this.portalRegistry::find)
                                .isPresent()
                );

        event.filterTickets(ticket -> affectPortal);
        event.filterTargetPositions(position -> affectPortal);
    }

}
