package cosmos.listeners.perworld;

import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.HealthsSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.util.Optional;

public class HealthsListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static Optional<Path> getHealthsPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.HEALTHS_DIRECTORY_NAME, fileName);
    }

    private static Optional<Path> getHealthsPath(Player player) {
        return getHealthsPath(player.getWorld(), player);
    }

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        getHealthsPath(from, player).ifPresent(path -> HealthsSerializer.serialize(path, player.getHealthData()));
        getHealthsPath(to, player).flatMap(HealthsSerializer::deserialize).ifPresent(player::offer);
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                getHealthsPath(player).ifPresent(path -> HealthsSerializer.serialize(path, player.getHealthData()))
        );
    }
}
