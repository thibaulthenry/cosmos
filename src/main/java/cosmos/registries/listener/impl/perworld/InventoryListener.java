package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.perworld.GroupRegistry;
import cosmos.registries.serializer.impl.InventorySerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
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

@Singleton
public class InventoryListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final GroupRegistry groupRegistry;
    private final InventorySerializer serializer;

    @Inject
    public InventoryListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
        this.serializer = injector.getInstance(InventorySerializer.class);
    }

    @Listener
    public void onDisconnectServerSideConnectionEvent(final ServerSideConnectionEvent.Disconnect event, @First final ServerPlayer player) {
        this.save(player.world().key(), player);
    }

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.FALSE)
    public void onHarvestEntityEvent(final HarvestEntityEvent event, @First final ServerPlayer player) {
        this.save(player.world().key(), player);
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.share(player.world().key(), player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        final ResourceKey originalWorldKey = event.originalWorld().key();
        final ResourceKey destinationWorldKey = event.destinationWorld().key();
        this.save(originalWorldKey, player);

        if (!this.groupRegistry.find(Tuple.of(PerWorldFeatures.INVENTORY, originalWorldKey)).map(group -> group.contains(destinationWorldKey)).orElse(false)) {
            this.share(destinationWorldKey, player);
        }
    }

    @Listener
    public void onRespawnPlayerEvent(final RespawnPlayerEvent event, @First final ServerPlayer player) {
        this.share(event.destinationWorld().key(), player);
    }

    @Listener
    public void onStoppingServerEvent(final StoppingEngineEvent<Server> event) {
        this.save();
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers()
                .stream()
                .filter(player -> !player.isRemoved())
                .forEach(player -> this.save(player.world().key(), player));
    }

    private void save(final ResourceKey worldKey, final ServerPlayer player) {
        this.groupRegistry.find(Tuple.of(PerWorldFeatures.INVENTORY, worldKey))
                .orElse(Collections.singleton(worldKey))
                .stream()
                .map(key -> this.finderService.findCosmosPath(Directories.INVENTORIES, key, player))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(path -> this.serializer.serialize(path, new ExtendedInventoryData(player)));
    }

    private void share(final ResourceKey worldKey, final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.INVENTORIES, worldKey, player)
                .flatMap(this.serializer::deserialize)
                .orElse(new ExtendedInventoryData())
                .share(player);
    }

}
