package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.perworld.BypassRegistry;
import cosmos.registries.perworld.GroupRegistry;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.AudienceMessageEvent;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class ChatListener extends AbstractPerWorldListener {

    private final BypassRegistry bypassRegistry;
    private final GroupRegistry groupRegistry;

    @Inject
    public ChatListener(final Injector injector) {
        this.bypassRegistry = injector.getInstance(BypassRegistry.class);
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
    }

    @Listener
    public void onPlayerChatEvent(final AudienceMessageEvent event, @First final ServerPlayer player) {
        if (this.bypassRegistry.doesBypass(PerWorldFeatures.CHAT, player)) {
            return;
        }

        Collection<Audience> audiences = this.groupRegistry.find(Tuple.of(PerWorldFeatures.CHAT, player.world().key()))
                .orElse(Collections.singleton(player.world().key()))
                .stream()
                .map(Sponge.server().worldManager()::world)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(World::audiences)
                .flatMap(worldAudiences -> StreamSupport.stream(worldAudiences.spliterator(), false))
                .collect(Collectors.toList());

        Collection<Audience> bypassingPlayers = this.bypassRegistry.value(PerWorldFeatures.CHAT)
                .stream()
                .map(playerId -> Sponge.server().player(playerId.first()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Audience::audience)
                .collect(Collectors.toList());

        audiences.addAll(bypassingPlayers);

        event.setAudience(Audience.audience(audiences));
    }

}
