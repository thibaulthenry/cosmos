package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.PortalDispatcherRegistry;
import cosmos.registries.portal.PortalFrameRegistry;
import cosmos.registries.portal.PortalSelectionRegistry;
import cosmos.services.portal.PortalService;
import cosmos.services.transportation.TransportationService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.entity.MovementType;
import org.spongepowered.api.event.cause.entity.MovementTypes;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;

@Singleton
public class PortalDispatcherListener extends AbstractListener {

    private final PortalDispatcherRegistry portalDispatcherRegistry;

    @Inject
    public PortalDispatcherListener(final Injector injector) {
        this.portalDispatcherRegistry = injector.getInstance(PortalDispatcherRegistry.class);
    }

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onPreChangeEntityWorldEvent(final ChangeEntityWorldEvent.Pre event) {
        final Optional<MovementType> optionalMovementType = event.getContext().get(EventContextKeys.MOVEMENT_TYPE);

        if (!optionalMovementType.isPresent() || !MovementTypes.PORTAL.get().equals(optionalMovementType.get())) {
            return;
        }

        final Optional<PortalType> optionalPortalType = event.getCause().first(PortalType.class);

        if (!optionalPortalType.isPresent()) {
            return;
        }

        this.portalDispatcherRegistry.find(event.getOriginalWorld().getKey())
                .flatMap(portalDispatcher -> portalDispatcher.findLinkedWorld(optionalPortalType.get()))
                .ifPresent(event::setDestinationWorld);
    }

}
