package cosmos.listeners.perworld;

import cosmos.listeners.ScheduledSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.AdvancementsSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.util.Optional;

public class AdvancementsListener extends AbstractPerWorldListener implements ScheduledSaveListener {

    private static Optional<Path> getAdvancementsPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.ADVANCEMENTS_DIRECTORY_NAME, fileName);
    }

    private static Optional<Path> getAdvancementsPath(Player player) {
        return getAdvancementsPath(player.getWorld(), player);
    }

    @Listener
    @SuppressWarnings("MethodMayBeStatic")
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        getAdvancementsPath(from, player).ifPresent(path -> AdvancementsSerializer.serialize(path, player));
        getAdvancementsPath(to, player).ifPresent(path -> AdvancementsSerializer.deserialize(path, player));
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                getAdvancementsPath(player).ifPresent(path -> AdvancementsSerializer.serialize(path, player))
        );
    }

}
