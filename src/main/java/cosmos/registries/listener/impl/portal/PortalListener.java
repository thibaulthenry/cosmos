package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.PortalDispatcherRegistry;
import cosmos.registries.portal.PortalRegistry;
import cosmos.registries.portal.PortalSelectionRegistry;
import cosmos.services.perworld.ScoreboardsService;
import cosmos.services.portal.PortalService;
import cosmos.services.transportation.TransportationService;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
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
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collections;
import java.util.Optional;

@Singleton
public class PortalListener extends AbstractListener {

    private final PortalRegistry portalRegistry;
    private final PortalDispatcherRegistry portalDispatcherRegistry;
    private final PortalSelectionRegistry portalSelectionRegistry;
    private final PortalService portalService;
    private final TransportationService transportationService;

    @Inject
    public PortalListener(final Injector injector) {
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.portalDispatcherRegistry = injector.getInstance(PortalDispatcherRegistry.class);
        this.portalSelectionRegistry = injector.getInstance(PortalSelectionRegistry.class);
        this.portalService = injector.getInstance(PortalService.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onPreChangeEntityWorldEvent(final MoveEntityEvent event, @First final ServerPlayer player) {
        final LocatableBlock locatableBlock = player.getServerLocation().asLocatableBlock();

        if (!locatableBlock.getBlockState().getType().isAnyOf(BlockTypes.VOID_AIR.get())) {
            return;
        }

        this.portalRegistry.find(locatableBlock)
                .flatMap(CosmosPortal::getDestination)
                .ifPresent(destination -> {
                    event.setCancelled(true);
                    this.transportationService.teleport(player, destination, false);
                });
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

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onSecondaryInteractBlockEvent(final InteractBlockEvent.Secondary event, @First final ServerPlayer player) {
        if (!GameModes.CREATIVE.get().equals(player.gameMode().get())) {
            return;
        }

        final BlockSnapshot blockSnapshot = event.getBlock();

        if (!(blockSnapshot.getLocation().isPresent() && blockSnapshot.getState().getType().isAnyOf(BlockTypes.BARRIER.get()))) {
            return;
        }

        event.setUseItemResult(Tristate.TRUE);

        if (!player.getItemInHand(HandTypes.MAIN_HAND).getType().isAnyOf(ItemTypes.FLINT_AND_STEEL.get())) {
            return;
        }

        event.setCancelled(true);

        final ServerLocation location = blockSnapshot.getLocation().get().add(0.5, 0, 0.5);

        this.portalService.highlight(Ticks.of(20), "cosmos-portal-highlight", location);

        if (player.get(Keys.IS_SNEAKING).orElse(false)) {
            this.portalSelectionRegistry.remove(player.getUniqueId(), location);
        } else {
            this.portalSelectionRegistry.add(player.getUniqueId(), location);
        }
    }

}
