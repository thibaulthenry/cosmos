package cosmos.listeners.perworld;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.channel.MessageChannel;

public class ChatsListener extends AbstractPerWorldListener {

    @Listener
    public void onMessageSent(MessageChannelEvent.Chat event, @First Player player) {
        event.setChannel(MessageChannel.world(player.getWorld()));
    }

}
