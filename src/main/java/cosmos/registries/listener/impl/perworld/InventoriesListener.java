package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.serializer.impl.InventoriesSerializer;
import cosmos.services.io.FinderService;
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

@Singleton
public class InventoriesListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final InventoriesSerializer inventoriesSerializer;

    @Inject
    public InventoriesListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.inventoriesSerializer = injector.getInstance(InventoriesSerializer.class);
    }

    @Listener
    public void onDisconnectServerSideConnectionEvent(final ServerSideConnectionEvent.Disconnect event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.INVENTORIES, player)
                .ifPresent(path -> this.inventoriesSerializer.serialize(path, new ExtendedInventoryData(player)));
    }

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.FALSE)
    public void onHarvestEntityEvent(final HarvestEntityEvent event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.INVENTORIES, player.world(), player)
                .ifPresent(path -> this.inventoriesSerializer.serialize(path, new ExtendedInventoryData(player)));
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.INVENTORIES, player)
                .flatMap(this.inventoriesSerializer::deserialize)
                .orElse(new ExtendedInventoryData())
                .share(player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.INVENTORIES, event.originalWorld(), player)
                .ifPresent(path -> this.inventoriesSerializer.serialize(path, new ExtendedInventoryData(player)));

        this.finderService.findCosmosPath(Directories.INVENTORIES, event.destinationWorld(), player)
                .flatMap(this.inventoriesSerializer::deserialize)
                .orElse(new ExtendedInventoryData())
                .share(player);
    }

    @Listener
    public void onRespawnPlayerEvent(final RespawnPlayerEvent event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.INVENTORIES, event.destinationWorld(), player)
                .flatMap(this.inventoriesSerializer::deserialize)
                .orElse(new ExtendedInventoryData())
                .share(player);
    }

    @Listener
    public void onStoppingServerEvent(final StoppingEngineEvent<Server> event) {
        this.save();
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers().forEach(player ->
                this.finderService.findCosmosPath(Directories.INVENTORIES, player)
                        .ifPresent(path -> this.inventoriesSerializer.serialize(path, new ExtendedInventoryData(player)))
        );
    }

}
