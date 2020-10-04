package cosmos.listeners.time;

import cosmos.commands.time.IgnorePlayersSleeping;
import cosmos.listeners.AbstractListener;
import cosmos.statics.config.Config;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

public class IgnorePlayersSleepingListener extends AbstractListener {

    @Listener
    public void onWorldLoad(LoadWorldEvent event) {
        WorldProperties worldProperties = event.getTargetWorld().getProperties();

        if (Config.isIgnorePlayersSleepingWorldEnabled(worldProperties) && !IgnorePlayersSleeping.doesIgnorePlayersSleeping(worldProperties)) {
            IgnorePlayersSleeping.enableSleepIgnorance(worldProperties);
        }
    }

    @Listener
    @SuppressWarnings("MethodMayBeStatic")
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        player.setSleepingIgnored(IgnorePlayersSleeping.doesIgnorePlayersSleeping(to.getProperties()));
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @First Player player) {
        player.setSleepingIgnored(IgnorePlayersSleeping.doesIgnorePlayersSleeping(player.getWorld().getProperties()));
    }
}
