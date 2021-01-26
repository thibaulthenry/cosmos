package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.services.perworld.InventoriesService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.entity.HarvestEntityEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

@Singleton
public class InventoriesListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final InventoriesService inventoriesService;
    private final SerializerProvider serializerProvider;

    @Inject
    public InventoriesListener(final Injector injector) {
        this.inventoriesService = injector.getInstance(InventoriesService.class);
        this.serializerProvider = injector.getInstance(SerializerProvider.class);
    }

    @Listener
    public void onDisconnectServerSideConnectionEvent(final ServerSideConnectionEvent.Disconnect event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(player)
                .ifPresent(path -> this.serializerProvider.inventories().serialize(path, new ExtendedInventoryData(player)));
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(player)
                .flatMap(path -> this.serializerProvider.inventories().deserialize(path))
                .orElse(new ExtendedInventoryData())
                .share(player);
    }

    @Listener(order = Order.POST)
    public void onHarvestEntityEvent(final HarvestEntityEvent event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(player.getWorld(), player)
                .ifPresent(path -> this.serializerProvider.inventories().serialize(path, new ExtendedInventoryData(player)));
    }

    @Listener
    public void onRepositionChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.inventories().serialize(path, new ExtendedInventoryData(player)));
        this.inventoriesService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.inventories().deserialize(path))
                .orElse(new ExtendedInventoryData())
                .share(player);
    }

    @Listener
    public void onPostRespawnPlayerEvent(final RespawnPlayerEvent event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.inventories().deserialize(path))
                .orElse(new ExtendedInventoryData())
                .share(player);
    }

    @Listener
    public void onStoppingServerEvent(final StoppingEngineEvent<Server> event) {
        this.save();
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                this.inventoriesService.getPath(player)
                        .ifPresent(path -> this.serializerProvider.inventories().serialize(path, new ExtendedInventoryData(player)))
        );
    }

}
