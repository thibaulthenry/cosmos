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
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Singleton
public class PortalGenerationListener extends AbstractListener {

    private final PortalSelectionRegistry portalSelectionRegistry;
    private final PortalService portalService;

    @Inject
    public PortalGenerationListener(final Injector injector) {
        this.portalSelectionRegistry = injector.getInstance(PortalSelectionRegistry.class);
        this.portalService = injector.getInstance(PortalService.class);
    }

    private Set<ServerLocation> collectPlaceholders(final ServerPlayer player, final ServerLocation clickLocation) {
        final Set<ServerLocation> locations = new HashSet<>();

        if (player.get(Keys.IS_SNEAKING).orElse(false)) {
            locations.add(clickLocation);
            return locations;
        }

        final LinkedList<ServerLocation> queue = new LinkedList<>();
        queue.add(clickLocation);

        while (!queue.isEmpty() && locations.size() <= 100) {
            final ServerLocation location = queue.removeFirst();

            if (locations.contains(location)) {
                continue;
            }

            locations.add(location);

            Stream.of(
                    location.add(Vector3i.UNIT_X),
                    location.add(Vector3i.UNIT_Y),
                    location.add(Vector3i.UNIT_Z),
                    location.add(Vector3i.UNIT_X.negate()),
                    location.add(Vector3i.UNIT_Y.negate()),
                    location.add(Vector3i.UNIT_Z.negate())
            )
                    .filter(neighbor -> neighbor.blockType().isAnyOf(BlockTypes.BARRIER.get()))
                    .filter(neighbor -> !locations.contains(neighbor))
                    .forEach(queue::add);
        }

        return locations;
    }

    private Optional<ServerLocation> findPlaceholderLocation(final InteractBlockEvent event, final ServerPlayer player) {
        if (!GameModes.CREATIVE.get().equals(player.gameMode().get())) {
            return Optional.empty();
        }

        final BlockSnapshot blockSnapshot = event.block();

        if (!(blockSnapshot.location().isPresent() && blockSnapshot.state().type().isAnyOf(BlockTypes.BARRIER.get()))) {
            return Optional.empty();
        }

        if (!player.itemInHand(HandTypes.MAIN_HAND).type().isAnyOf(ItemTypes.FLINT_AND_STEEL.get())) {
            return Optional.empty();
        }

        if (event instanceof Cancellable) {
            ((Cancellable) event).setCancelled(true);
        }

        return Optional.of(blockSnapshot.location().get());
    }

    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onSecondaryInteractBlockEvent(final InteractBlockEvent.Secondary event, @First final ServerPlayer player) {
        this.findPlaceholderLocation(event, player)
                .map(location -> this.collectPlaceholders(player, location))
                .ifPresent(locations -> locations.forEach(location -> {
                    final BlockType blockType = this.portalSelectionRegistry.add(player.uniqueId(), location)
                            ? BlockTypes.LIME_STAINED_GLASS.get()
                            : BlockTypes.GLASS.get();

                    this.portalService.highlight(blockType, Ticks.of(40), "cosmos-portal-highlight", location.add(0.5, 0, 0.5));
                }));
    }


    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onStartPrimaryInteractBlockEvent(final InteractBlockEvent.Primary.Start event, @First final ServerPlayer player) {
        this.findPlaceholderLocation(event, player)
                .map(location -> this.collectPlaceholders(player, location))
                .ifPresent(locations -> locations.forEach(location -> {
                    final BlockType blockType = this.portalSelectionRegistry.remove(player.uniqueId(), location)
                            ? BlockTypes.RED_STAINED_GLASS.get()
                            : BlockTypes.GLASS.get();

                    this.portalService.highlight(blockType, Ticks.of(40), "cosmos-portal-highlight", location.add(0.5, 0, 0.5));
                }));
    }

}
