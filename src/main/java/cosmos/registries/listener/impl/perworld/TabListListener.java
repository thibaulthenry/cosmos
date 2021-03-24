package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class TabListListener extends AbstractPerWorldListener {

    private void addEntrySafely(final ServerPlayer addedPlayer, final ServerPlayer affectedPlayer) {
        final TabList tabList = affectedPlayer.tabList();

        final TabListEntry addedTabListEntry = TabListEntry.builder()
                .list(tabList)
                .profile(addedPlayer.profile())
                .gameMode(addedPlayer.gameMode().get())
                .build();

        if (tabList.entry(addedPlayer.uniqueId()).isPresent()) {
            return;
        }

        try {
            tabList.addEntry(addedTabListEntry);
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while add entry to player tab list", e);
        }
    }

    private void clearTabList(final ServerPlayer player) {
        final Collection<UUID> onJoinerTabList = player.tabList().entries()
                .stream()
                .map(TabListEntry::profile)
                .map(GameProfile::uniqueId)
                .filter(uuid -> !player.uniqueId().equals(uuid))
                .collect(Collectors.toList());

        onJoinerTabList.forEach(uuid -> player.tabList().removeEntry(uuid));
    }

    private void joinWorldTabList(final ServerWorld world, final ServerPlayer joiningPlayer) {
        world.players().forEach(player -> {
            addEntrySafely(joiningPlayer, player);
            addEntrySafely(player, joiningPlayer);
        });
    }

    private void leaveAllTabList(final ServerPlayer targetPlayer) {
        Sponge.server().worldManager().worlds()
                .stream()
                .map(ServerWorld::players)
                .flatMap(Collection::stream)
                .filter(player -> !player.uniqueId().equals(targetPlayer.uniqueId()))
                .forEach(player -> player.tabList().removeEntry(targetPlayer.uniqueId()));
    }

    private void leaveWorldTabList(final ServerWorld world, final ServerPlayer leavingPlayer) {
        world.players()
                .stream()
                .filter(player -> !player.uniqueId().equals(leavingPlayer.uniqueId()))
                .forEach(player -> player.tabList().removeEntry(leavingPlayer.uniqueId()));
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.leaveAllTabList(player);
        this.clearTabList(player);
        this.joinWorldTabList(player.world(), player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.leaveWorldTabList(event.originalWorld(), player);
        this.clearTabList(player);
        this.joinWorldTabList(event.destinationWorld(), player);
    }

    @Override
    public void start() {
        Sponge.server().onlinePlayers().forEach(player -> {
            this.clearTabList(player);
            this.joinWorldTabList(player.world(), player);
        });
    }

    @Override
    public void stop() {
        Sponge.server().onlinePlayers().forEach(affectedPlayer -> {
            this.clearTabList(affectedPlayer);
            Sponge.server().onlinePlayers().forEach(addedPlayer -> this.addEntrySafely(addedPlayer, affectedPlayer));
        });
    }

}
