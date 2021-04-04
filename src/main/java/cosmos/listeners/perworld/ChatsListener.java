package cosmos.listeners.perworld;

import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.PerWorldCommands;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Tuple;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChatsListener extends AbstractPerWorldListener {

    @Listener
    public void onMessageSent(MessageChannelEvent.Chat event, @First Player player) {
        if (Bypass.doesBypass(PerWorldCommands.CHATS, player)) {
            return;
        }

        Collection<MessageChannel> messageChannels = GroupRegister.find(Tuple.of(PerWorldCommands.CHATS, player.getWorld().getName()))
                .orElse(Collections.singleton(player.getWorld().getName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(MessageChannel::world)
                .collect(Collectors.toList());

        Collection<MessageReceiver> bypassingPlayer = Bypass.getAllBypass(PerWorldCommands.CHATS)
                .stream()
                .map(playerId -> Sponge.getServer().getPlayer(playerId.getFirst()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        messageChannels.add(MessageChannel.fixed(bypassingPlayer));

        event.setChannel(MessageChannel.combined(messageChannels));
    }

}
