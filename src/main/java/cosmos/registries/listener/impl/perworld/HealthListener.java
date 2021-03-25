package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.data.serializable.impl.HealthData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.perworld.GroupRegistry;
import cosmos.registries.serializer.impl.HealthSerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.entity.HarvestEntityEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.Tuple;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Singleton
public class HealthListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final GroupRegistry groupRegistry;
    private final HealthSerializer healthSerializer;

    @Inject
    public HealthListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
        this.healthSerializer = injector.getInstance(HealthSerializer.class);
    }

    @Listener
    public void onDisconnectServerSideConnectionEvent(final ServerSideConnectionEvent.Disconnect event, @First final ServerPlayer player) {
        this.save(player.world().key(), player);
    }

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.FALSE)
    public void onHarvestEntityEvent(final HarvestEntityEvent event, @First final ServerPlayer player) {
        this.save(player.world().key(), player, new HealthData());
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.share(player.world().key(), player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        final ResourceKey originalWorldKey = event.originalWorld().key();
        this.save(originalWorldKey, player);
        final Optional<Set<ResourceKey>> optionalGroup = this.groupRegistry.find(Tuple.of(Directories.HEALTHS, originalWorldKey));
        final ResourceKey destinationWorldKey = event.destinationWorld().key();

        if (optionalGroup.map(group -> group.contains(destinationWorldKey)).orElse(false)) {
            return;
        }

        this.share(destinationWorldKey, player);
    }

    @Listener
    public void onRespawnPlayerEvent(final RespawnPlayerEvent.Post event, @First final ServerPlayer player) {
        this.share(event.destinationWorld().key(), event.entity());
    }

    @Listener
    public void onStoppingServerEvent(final StoppingEngineEvent<Server> event) {
        this.save();
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers().forEach(player -> this.save(player.world().key(), player));
    }

    private void save(final ResourceKey worldKey, final ServerPlayer player) {
        this.save(worldKey, player, new HealthData(player));
    }

    private void save(final ResourceKey worldKey, final ServerPlayer player, final HealthData data) {
        this.groupRegistry.find(Tuple.of("health", worldKey))
                .orElse(Collections.singleton(worldKey))
                .stream()
                .map(key -> this.finderService.findCosmosPath(Directories.HEALTHS, key, player))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(path -> this.healthSerializer.serialize(path, data));
    }

    private void share(final ResourceKey worldKey, final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.HEALTHS, worldKey, player)
                .flatMap(this.healthSerializer::deserialize)
                .orElse(new HealthData())
                .share(player);
    }

}
