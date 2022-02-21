package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.perworld.GroupRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class TabListListener extends AbstractPerWorldListener {

    private final GroupRegistry groupRegistry;

    @Inject
    public TabListListener(final Injector injector) {
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
    }

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

    private void clear(final ServerPlayer player) {
        player.tabList().entries()
                .stream()
                .map(TabListEntry::profile)
                .map(GameProfile::uniqueId)
                .filter(uuid -> !player.uniqueId().equals(uuid))
                .collect(Collectors.toList())
                .forEach(uuid -> player.tabList().removeEntry(uuid));
    }

    private void join(final ServerWorld world, final ServerPlayer joiningPlayer) {
        this.groupRegistry.find(Tuple.of(PerWorldFeatures.TAB_LIST, world.key()))
                .orElse(Collections.singleton(world.key()))
                .stream()
                .map(key -> Sponge.server().worldManager().world(key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ServerWorld::players)
                .flatMap(Collection::stream)
                .forEach(player -> {
                    addEntrySafely(joiningPlayer, player);
                    addEntrySafely(player, joiningPlayer);
                });
    }

    private void leave(final ServerPlayer leavingPlayer) {
        Sponge.server().onlinePlayers().forEach(player -> player.tabList().removeEntry(leavingPlayer.uniqueId()));
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.share(player.world(), player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        if (!this.groupRegistry.find(Tuple.of(PerWorldFeatures.TAB_LIST, event.originalWorld().key())).map(group -> group.contains(event.destinationWorld().key())).orElse(false)) {
            this.share(event.destinationWorld(), player);
        }
    }

    @Listener
    public void onRespawnPlayerEvent(final RespawnPlayerEvent.Post event, @First final ServerPlayer player) {
        if (!this.groupRegistry.find(Tuple.of(PerWorldFeatures.TAB_LIST, event.originalWorld().key())).map(group -> group.contains(event.destinationWorld().key())).orElse(false)) {
            this.share(event.destinationWorld(), player);
        }
    }

    private void share(final ServerWorld world, final ServerPlayer player) {
        this.leave(player);
        this.clear(player);
        this.join(world, player);
    }

    @Override
    public void start() {
        Sponge.server().onlinePlayers().forEach(player -> {
            this.clear(player);
            this.join(player.world(), player);
        });
    }

    @Override
    public void stop() {
        Sponge.server().onlinePlayers().forEach(affectedPlayer -> {
            this.clear(affectedPlayer);
            Sponge.server().onlinePlayers().forEach(addedPlayer -> this.addEntrySafely(addedPlayer, affectedPlayer));
        });
    }

}
