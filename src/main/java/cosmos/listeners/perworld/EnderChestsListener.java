package cosmos.listeners.perworld;

import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.config.Config;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.EnderChestsSerializer;
import cosmos.statics.serializers.InventoriesSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class EnderChestsListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static Optional<Path> getEnderChestsPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.ENDER_CHESTS_DIRECTORY_NAME, fileName);
    }

    private static Optional<Path> getEnderChestsPath(UUID worldUUID, UUID playerUUID) {
        String fileName = worldUUID + "_" + playerUUID + ".dat";
        return FinderFile.getCosmosPath(FinderFile.ENDER_CHESTS_DIRECTORY_NAME, fileName);
    }

    @Listener
    public void onDisconnectClientConnectionEvent(ClientConnectionEvent.Disconnect event, @First Player player) {
        save(player.getWorld(), player);
    }

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.FALSE)
    public void onDeathDestructEntityEventEvent(DestructEntityEvent.Death event, @First Player player) {
        save(player.getWorld(), player);
    }

    @Listener
    public void onJoinClientConnectionEvent(ClientConnectionEvent.Join event, @First Player player) {
        share(player.getWorld(), player);
    }

    @Listener
    @SuppressWarnings("MethodMayBeStatic")
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        save(from, player);

        if (!GroupRegister.find(Tuple.of(PerWorldCommands.ENDER_CHESTS, from.getName())).map(group -> group.contains(to.getName())).orElse(false)) {
            share(to, player);
        }
    }

    @Listener
    public void onRespawnPlayerEvent(RespawnPlayerEvent event, @First Player player) {
        share(event.getToTransform().getExtent(), event.getTargetEntity());
    }

    @Listener
    public void onStoppingServerEvent(GameStoppingServerEvent event) {
        save();
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers()
                .stream()
                .filter(player -> !player.isRemoved())
                .forEach(player -> save(player.getWorld(), player));
    }

    private void save(World world, Player player) {
        if (Bypass.doesBypass(PerWorldCommands.ENDER_CHESTS, player)) {
            return;
        }

        GroupRegister.find(Tuple.of(PerWorldCommands.ENDER_CHESTS, world.getName()))
                .orElse(Collections.singleton(world.getName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName).flatMap(w -> getEnderChestsPath(w, player)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(path -> EnderChestsSerializer.serialize(path, player));
    }

    private void share(World world, Player player) {
        if (Bypass.doesBypass(PerWorldCommands.ENDER_CHESTS, player)) {
            return;
        }

        player.getEnderChestInventory().clear();
        getEnderChestsPath(world, player).ifPresent(path -> EnderChestsSerializer.deserialize(path, player));
    }

    public void onStart() {
        if (!Config.isPersistedOnActivation(PerWorldCommands.ENDER_CHESTS.getListenerClass())) {
            return;
        }

        Optional<UUID> optionalDefaultWorldUUID = Sponge.getServer().getDefaultWorld().map(WorldProperties::getUniqueId);

        if (!optionalDefaultWorldUUID.isPresent()) {
            return;
        }

        UUID defaultWorldUUID = optionalDefaultWorldUUID.get();

        FinderFile.streamPlayerData().forEach(playerDataPath -> {
            UUID playerUUID;

            try {
                String fileName = playerDataPath.getFileName().toString();

                if (!fileName.endsWith(".dat")) {
                    return;
                }

                int index = fileName.lastIndexOf('.');

                if (index == -1) {
                    playerUUID = UUID.fromString(fileName);
                } else {
                    playerUUID = UUID.fromString(fileName.substring(0, index));
                }
            } catch (Exception ignored) {
                return;
            }

            Optional<Path> optionalPath = getEnderChestsPath(defaultWorldUUID, playerUUID);

            if (!optionalPath.isPresent()) {
                return;
            }

            Path path = optionalPath.get();

            if (Files.exists(path)) {
                return;
            }

            EnderChestsSerializer.serializePlayerData(path, playerDataPath);
        });
    }

}
