package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.selector.Selector;
import org.spongepowered.api.command.selector.SelectorType;
import org.spongepowered.api.command.selector.SelectorTypes;
import org.spongepowered.api.entity.living.player.PlayerChatRouter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.criteria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.util.Range;

@Singleton
public class ChatsListener extends AbstractPerWorldListener {

    @Listener
    public void onPlayerChatEvent(final PlayerChatEvent event, @First final ServerPlayer player) {
        final Audience audience = Audience.audience(player.getServerLocation().getWorld().audiences());
        // todo
        event.setChatRouter(PlayerChatRouter.toAudience(audience));
//
//
//        //Score score = Cosmos.getServices().perWorld().scoreboards().getOrCreateScoreboard(player.getWorld())
//        Team t = Team.builder().name("team").build();
//        Selector selector = Selector.builder().team(t).applySelectorType(SelectorTypes.ALL_PLAYERS).build();
//
//        selector.select(player);
    }

}
