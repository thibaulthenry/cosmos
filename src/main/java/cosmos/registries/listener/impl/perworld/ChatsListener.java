package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.PlayerChatRouter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.PlayerChatEvent;

@Singleton
public class ChatsListener extends AbstractListener implements ToggleListener {

    @Listener
    public void onPlayerChatEvent(final PlayerChatEvent event, @First final ServerPlayer player) {
        final Audience audience = Audience.audience(player.getServerLocation().getWorld().audiences());
        event.setChatRouter(PlayerChatRouter.toAudience(audience));
    }

}
