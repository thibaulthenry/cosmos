package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.services.perworld.InventoriesService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

@Singleton
public class InventoriesListener extends AbstractListener implements ScheduledAsyncSaveListener, ToggleListener {

    @Inject
    private InventoriesService inventoriesService;

    @Inject
    private SerializerProvider serializerProvider;

    @Listener
    public void onDisconnectServerSideConnectionEvent(final ServerSideConnectionEvent.Disconnect event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(player)
                .ifPresent(path -> this.serializerProvider.inventories().serialize(path, new ExtendedInventoryData(player)));
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(player)
                .flatMap(path -> this.serializerProvider.inventories().deserialize(path))
                .ifPresent(data -> data.offer(player));
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.inventoriesService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.inventories().serialize(path, new ExtendedInventoryData(player)));
        // todo clear everything or apply air
        this.inventoriesService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.inventories().deserialize(path))
                .ifPresent(data -> data.offer(player));
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
