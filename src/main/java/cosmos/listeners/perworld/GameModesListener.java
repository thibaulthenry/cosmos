package cosmos.listeners.perworld;

import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.PerWorldCommands;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;

import java.util.Collections;

public class GameModesListener extends AbstractPerWorldListener {

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        share(to, player);
    }

    @Override
    public void onStart() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> share(player.getWorld(), player));
    }

    @Override
    public void onStart(Player player) {
        share(player.getWorld(), player);
    }

    private void share(World world, Player player) {
        if (Bypass.doesBypass(PerWorldCommands.GAME_MODES, player)) {
            return;
        }

        String groupedWorldName = GroupRegister.find(Tuple.of(PerWorldCommands.GAME_MODES, world.getName()))
                .orElse(Collections.singleton(world.getName()))
                .iterator()
                .next();

        world = Sponge.getServer().getWorld(groupedWorldName).orElse(world);

        player.offer(Keys.GAME_MODE, world.getProperties().getGameMode());
    }

}
