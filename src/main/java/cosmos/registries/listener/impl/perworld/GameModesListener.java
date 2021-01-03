package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class GameModesListener extends AbstractListener implements ToggleListener {

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        player.offer(Keys.GAME_MODE, event.getDestinationWorld().getProperties().gameMode());
    }

    @Override
    public void start() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                player.offer(Keys.GAME_MODE, player.getWorld().getProperties().gameMode())
        );
    }

}
