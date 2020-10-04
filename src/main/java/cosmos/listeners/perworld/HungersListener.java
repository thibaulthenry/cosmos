package cosmos.listeners.perworld;

import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.HungersSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.util.Optional;

public class HungersListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static Optional<Path> getHungersPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.HUNGERS_DIRECTORY_NAME, fileName);
    }

    private static Optional<Path> getHungersPath(Player player) {
        return getHungersPath(player.getWorld(), player);
    }

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        getHungersPath(from, player).ifPresent(path -> HungersSerializer.serialize(path, player.getFoodData()));
        getHungersPath(to, player).flatMap(HungersSerializer::deserialize).ifPresent(player::offer);
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                getHungersPath(player).ifPresent(path -> HungersSerializer.serialize(path, player.getFoodData()))
        );
    }
}
