package cosmos.listeners.perworld;

import cosmos.Cosmos;
import cosmos.constants.Outputs;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.UUID;
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
        } catch (IllegalArgumentException | IllegalStateException ignored) {
            Cosmos.sendConsole(Outputs.JOINING_TAB_LIST.asText(addedPlayer.getName(), affectedPlayer.getName()));
        }
    }

    private static void joinWorldTabList(World world, Player joiningPlayer) {
        world.getPlayers().forEach(player -> {
            addEntrySafely(joiningPlayer, player);
            addEntrySafely(player, joiningPlayer);
        });
    }

    private static void clearTabList(Player player) {
        Collection<UUID> onJoinerTabList = player.getTabList().getEntries().stream()
                .map(TabListEntry::getProfile)
                .map(GameProfile::getUniqueId)
                .filter(uuid -> !player.getUniqueId().equals(uuid))
                .collect(Collectors.toList());

        onJoinerTabList.forEach(uuid -> player.getTabList().removeEntry(uuid));
    }

    private static void leaveWorldTabList(World world, Player leavingPlayer) {
        world.getPlayers()
                .stream()
                .filter(player -> !player.getUniqueId().equals(leavingPlayer.getUniqueId()))
                .forEach(player -> player.getTabList().removeEntry(leavingPlayer.getUniqueId()));
    }

    private static void leaveAllTabList(Player targetPlayer) {
        Sponge.getServer().getWorlds()
                .stream()
                .map(World::getPlayers)
                .flatMap(Collection::stream)
                .filter(player -> !player.getUniqueId().equals(targetPlayer.getUniqueId()))
                .forEach(player -> player.getTabList().removeEntry(targetPlayer.getUniqueId()));
    }

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        leaveWorldTabList(from, player);
        clearTabList(player);
        joinWorldTabList(to, player);
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @First Player player) {
        leaveAllTabList(player);
        clearTabList(player);
        joinWorldTabList(player.getWorld(), player);
    }

    @Override
    public void onStart() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> {
            clearTabList(player);
            joinWorldTabList(player.getWorld(), player);
        });
    }

    @Override
    public void onStop() {
        Sponge.getServer().getOnlinePlayers().forEach(affectedPlayer -> {
            clearTabList(affectedPlayer);
            Sponge.getServer().getOnlinePlayers().forEach(addedPlayer -> addEntrySafely(addedPlayer, affectedPlayer));
        });
    }
}
