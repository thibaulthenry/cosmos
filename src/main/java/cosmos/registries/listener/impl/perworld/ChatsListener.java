package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.entity.living.player.PlayerChatRouter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.api.util.Tristate;

@Singleton
public class ChatsListener extends AbstractPerWorldListener {

    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onPlayerChatEvent(final PlayerChatEvent event, @First final ServerPlayer player) {
        final Audience audience = Audience.audience(player.serverLocation().world().audiences());
        event.setChatRouter(PlayerChatRouter.toAudience(audience));
        // TODO Make an issue or ask for implementation because it's not working
    }

}
