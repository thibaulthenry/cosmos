package cosmos.listeners.perworld;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

public class GameModesListener extends AbstractPerWorldListener {

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        player.offer(Keys.GAME_MODE, to.getProperties().getGameMode());
    }

    @Override
    public void onStart() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> player.offer(Keys.GAME_MODE, player.getWorld().getProperties().getGameMode()));
    }
}
