package cosmos.listeners.perworld;

import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.InventoriesSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.util.Optional;

public class InventoriesListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static Optional<Path> getInventoriesPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.INVENTORIES_DIRECTORY_NAME, fileName);
    }

    private static Optional<Path> getInventoriesPath(Player player) {
        return getInventoriesPath(player.getWorld(), player);
    }

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        getInventoriesPath(from, player).ifPresent(path -> InventoriesSerializer.serialize(path, player));
        player.getInventory().clear();
        getInventoriesPath(to, player).ifPresent(path -> InventoriesSerializer.deserialize(path, player));
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @First Player player) {
        getInventoriesPath(player).ifPresent(path -> InventoriesSerializer.deserialize(path, player));
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event, @First Player player) {
        getInventoriesPath(player).ifPresent(path -> InventoriesSerializer.serialize(path, player));
    }

    @Listener
    public void onGameStoppingEvent(GameStoppingEvent event) {
        save();
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                getInventoriesPath(player).ifPresent(path -> InventoriesSerializer.serialize(path, player))
        );
    }
}
