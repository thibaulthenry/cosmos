package cosmos.listeners.perworld;

import cosmos.Cosmos;
import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.Outputs;
import cosmos.constants.PerWorldCommands;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class TabListsListener extends AbstractPerWorldListener {

    private static void addEntrySafely(Player addedPlayer, Player affectedPlayer) {
        TabList tabList = affectedPlayer.getTabList();

        TabListEntry addedTabListEntry = TabListEntry.builder()
                .list(tabList)
                .profile(addedPlayer.getProfile())
                .gameMode(addedPlayer.gameMode().get())
                .build();

        if (tabList.getEntry(addedPlayer.getUniqueId()).isPresent()) {
            return;
        }

        try {
            tabList.addEntry(addedTabListEntry);
        } catch (Exception e) {
            Cosmos.sendConsole(Outputs.JOINING_TAB_LIST.asText(addedPlayer.getName(), affectedPlayer.getName()));
        }
    }

    private static void clear(Player player) {
        if (Bypass.doesBypass(PerWorldCommands.TAB_LISTS, player)) {
            return;
        }

        player.getTabList().getEntries()
                .stream()
                .map(TabListEntry::getProfile)
                .map(GameProfile::getUniqueId)
                .filter(uuid -> !player.getUniqueId().equals(uuid))
                .collect(Collectors.toList())
                .forEach(uuid -> player.getTabList().removeEntry(uuid));
    }

    private static void join(World world, Player joiningPlayer) {
        GroupRegister.find(Tuple.of(PerWorldCommands.TAB_LISTS, world.getName()))
                .orElse(Collections.singleton(world.getName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(World::getPlayers)
                .flatMap(Collection::stream)
                .forEach(player -> {
                    if (!Bypass.doesBypass(PerWorldCommands.TAB_LISTS, player)) {
                        addEntrySafely(joiningPlayer, player);
                    }

                    if (!Bypass.doesBypass(PerWorldCommands.TAB_LISTS, joiningPlayer)) {
                        addEntrySafely(player, joiningPlayer);
                    }
                });
    }

    private static void leave(Player leavingPlayer) {
        Sponge.getServer().getOnlinePlayers()
                .stream()
                .filter(player -> !leavingPlayer.getUniqueId().equals(player.getUniqueId()))
                .forEach(player -> {
                    if (!Bypass.doesBypass(PerWorldCommands.TAB_LISTS, player)) {
                        player.getTabList().removeEntry(leavingPlayer.getUniqueId());
                    }
                });
    }

    @Listener
    public void onJoinServerSideConnectionEvent(ClientConnectionEvent.Join event, @First Player player) {
        this.share(player.getWorld(), player);
    }

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        if (!GroupRegister.find(Tuple.of(PerWorldCommands.TAB_LISTS, from.getName())).map(group -> group.contains(to.getName())).orElse(false)) {
            share(to, player);
        }
    }

    @Listener
    public void onRespawnPlayerEvent(RespawnPlayerEvent event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (!GroupRegister.find(Tuple.of(PerWorldCommands.TAB_LISTS, from.getName())).map(group -> group.contains(to.getName())).orElse(false)) {
            share(event.getToTransform().getExtent(), event.getOriginalPlayer());
        }
    }

    private void share(World world, Player player) {
        leave(player);
        clear(player);
        join(world, player);
    }

    @Override
    public void onStart() {
        Sponge.getServer().getOnlinePlayers().forEach(this::onStart);
    }

    @Override
    public void onStart(Player player) {
        clear(player);
        join(player.getWorld(), player);
    }

    @Override
    public void onStop() {
        Sponge.getServer().getOnlinePlayers().forEach(this::onStop);
    }

    @Override
    public void onStop(Player player) {
        clear(player);
        Sponge.getServer().getOnlinePlayers().forEach(addedPlayer -> addEntrySafely(addedPlayer, player));
    }

}
