package cosmos.listeners.perworld;

import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.ExperiencesSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.entity.ExperienceHolderData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.util.Optional;


public class ExperiencesListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static Optional<Path> getExperiencesPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.EXPERIENCES_DIRECTORY_NAME, fileName);
    }

    private static Optional<Path> getExperiencesPath(Player player) {
        return getExperiencesPath(player.getWorld(), player);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        getExperiencesPath(from, player).ifPresent(path ->
                ExperiencesSerializer.serialize(path, player.getOrCreate(ExperienceHolderData.class).get()));
        getExperiencesPath(to, player).flatMap(ExperiencesSerializer::deserialize).ifPresent(player::offer);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                getExperiencesPath(player).ifPresent(path ->
                        ExperiencesSerializer.serialize(path, player.getOrCreate(ExperienceHolderData.class).get())
                )
        );
    }
}
