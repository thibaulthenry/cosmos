package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.PortalSelectionRegistry;
import cosmos.services.portal.PortalService;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3i;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
public class PortalGenerationListener extends AbstractListener {

    private final PortalSelectionRegistry portalSelectionRegistry;
    private final PortalService portalService;

    @Inject
    public PortalGenerationListener(final Injector injector) {
        this.portalSelectionRegistry = injector.getInstance(PortalSelectionRegistry.class);
        this.portalService = injector.getInstance(PortalService.class);
    }

    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onInteractBlockEvent(final InteractBlockEvent.Secondary event, @First final ServerPlayer player) {
        this.findFrameClickLocation(event, player)
                .map(location -> this.getFrameLocations(player, location))
                .ifPresent(locations -> locations.forEach(location -> {
                    final BlockType blockType = this.portalSelectionRegistry.add(player.getUniqueId(), location)
                            ? BlockTypes.LIME_STAINED_GLASS.get() : BlockTypes.GLASS.get();
                    this.portalService.highlight(blockType, Ticks.of(20), "cosmos-portal-highlight", location.add(0.5, 0, 0.5));
                }));
    }


    @Listener
    @IsCancelled(value = Tristate.FALSE)
    public void onInteractBlockEvent(final InteractBlockEvent.Primary.Start event, @First final ServerPlayer player) {
        this.findFrameClickLocation(event, player)
                .map(location -> this.getFrameLocations(player, location))
                .ifPresent(locations -> locations.forEach(location -> {
                    final BlockType blockType = this.portalSelectionRegistry.remove(player.getUniqueId(), location)
                            ? BlockTypes.RED_STAINED_GLASS.get() : BlockTypes.GLASS.get();
                    this.portalService.highlight(blockType, Ticks.of(20), "cosmos-portal-highlight", location.add(0.5, 0, 0.5));
                }));
    }

    private Optional<ServerLocation> findFrameClickLocation(final InteractBlockEvent event, final ServerPlayer player) {
        if (!GameModes.CREATIVE.get().equals(player.gameMode().get())) {
            return Optional.empty();
        }

        final BlockSnapshot blockSnapshot = event.getBlock();

        if (!(blockSnapshot.getLocation().isPresent() && blockSnapshot.getState().getType().isAnyOf(BlockTypes.BARRIER.get()))) {
            return Optional.empty();

        }

        if (!player.getItemInHand(HandTypes.MAIN_HAND).getType().isAnyOf(ItemTypes.FLINT_AND_STEEL.get())) {
            return Optional.empty();
        }

        if (event instanceof Cancellable) {
            ((Cancellable) event).setCancelled(true);
        }

        return Optional.of(blockSnapshot.getLocation().get());
    }

    private Set<ServerLocation> getFrameLocations(final ServerPlayer player, final ServerLocation clickLocation) {
        final Set<ServerLocation> locations = new HashSet<>();

        if (player.get(Keys.IS_SNEAKING).orElse(false)) {
            locations.add(clickLocation);
        } else {
            this.collectNearbyBarrier(locations, clickLocation, 100);
        }

        return locations;
    }

    private void collectNearbyBarrier(final Set<ServerLocation> locations, final ServerLocation start, final int limit) {
        if (!start.getBlockType().isAnyOf(BlockTypes.BARRIER.get())) {
            return;
        }

        if (locations.contains(start) || locations.size() > limit) {
            return;
        }

        locations.add(start);

        this.collectNearbyBarrier(locations, start.add(Vector3i.UNIT_X), limit);
        this.collectNearbyBarrier(locations, start.add(Vector3i.UNIT_Y), limit);
        this.collectNearbyBarrier(locations, start.add(Vector3i.UNIT_Z), limit);
        this.collectNearbyBarrier(locations, start.add(Vector3i.UNIT_X.negate()), limit);
        this.collectNearbyBarrier(locations, start.add(Vector3i.UNIT_Y.negate()), limit);
        this.collectNearbyBarrier(locations, start.add(Vector3i.UNIT_Z.negate()), limit);
    }

}
