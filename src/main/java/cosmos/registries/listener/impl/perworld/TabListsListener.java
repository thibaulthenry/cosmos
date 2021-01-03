package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
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
public class TabListsListener extends AbstractListener implements ToggleListener {

    private void addEntrySafely(final ServerPlayer addedPlayer, final ServerPlayer affectedPlayer) {
        final TabList tabList = affectedPlayer.getTabList();

        final TabListEntry addedTabListEntry = TabListEntry.builder()
                .list(tabList)
                .profile(addedPlayer.getProfile())
                .gameMode(addedPlayer.gameMode().get())
                .build();

        if (tabList.getEntry(addedPlayer.getUniqueId()).isPresent()) {
            return;
        }

        try {
            tabList.addEntry(addedTabListEntry);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while add entry to player tab list", e);
        }
    }

    private void clearTabList(final ServerPlayer player) {
        final Collection<UUID> onJoinerTabList = player.getTabList().getEntries().stream()
                .map(TabListEntry::getProfile)
                .map(GameProfile::getUniqueId)
                .filter(uuid -> !player.getUniqueId().equals(uuid))
                .collect(Collectors.toList());

        onJoinerTabList.forEach(uuid -> player.getTabList().removeEntry(uuid));
    }

    private void joinWorldTabList(final ServerWorld world, final ServerPlayer joiningPlayer) {
        world.getPlayers().forEach(player -> {
            addEntrySafely(joiningPlayer, player);
            addEntrySafely(player, joiningPlayer);
        });
    }

    private void leaveWorldTabList(final ServerWorld world, final ServerPlayer leavingPlayer) {
        world.getPlayers()
                .stream()
                .filter(player -> !player.getUniqueId().equals(leavingPlayer.getUniqueId()))
                .forEach(player -> player.getTabList().removeEntry(leavingPlayer.getUniqueId()));
    }

    private void leaveAllTabList(final ServerPlayer targetPlayer) {
        Sponge.getServer().getWorldManager().worlds()
                .stream()
                .map(ServerWorld::getPlayers)
                .flatMap(Collection::stream)
                .filter(player -> !player.getUniqueId().equals(targetPlayer.getUniqueId()))
                .forEach(player -> player.getTabList().removeEntry(targetPlayer.getUniqueId()));
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.leaveAllTabList(player);
        this.clearTabList(player);
        this.joinWorldTabList(player.getWorld(), player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.leaveWorldTabList(event.getOriginalWorld(), player);
        this.clearTabList(player);
        this.joinWorldTabList(event.getDestinationWorld(), player);
    }

    @Override
    public void start() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> {
            this.clearTabList(player);
            this.joinWorldTabList(player.getWorld(), player);
        });
    }

    @Override
    public void stop() {
        Sponge.getServer().getOnlinePlayers().forEach(affectedPlayer -> {
            this.clearTabList(affectedPlayer);
            Sponge.getServer().getOnlinePlayers().forEach(addedPlayer -> this.addEntrySafely(addedPlayer, affectedPlayer));
        });
    }

}
