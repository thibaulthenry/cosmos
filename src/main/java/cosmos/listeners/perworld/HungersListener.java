package cosmos.listeners.perworld;

import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.DefaultData;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.config.Config;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.ExperiencesSerializer;
import cosmos.statics.serializers.HungersSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;
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

public class HungersListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static Optional<Path> getHungersPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.HUNGERS_DIRECTORY_NAME, fileName);
    }

    private static Optional<Path> getHungersPath(UUID worldUUID, UUID playerUUID) {
        String fileName = worldUUID + "_" + playerUUID + ".dat";
        return FinderFile.getCosmosPath(FinderFile.HUNGERS_DIRECTORY_NAME, fileName);
    }

    @Listener
    public void onDisconnectClientConnectionEvent(ClientConnectionEvent.Disconnect event, @First Player player) {
        save(player.getWorld(), player);
    }

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.FALSE)
    public void onDeathDestructEntityEventEvent(DestructEntityEvent.Death event, @First Player player) {
        Sponge.getDataManager()
                .deserialize(FoodData.class, DefaultData.DEFAULT_FOOD_DATA)
                .ifPresent(foodData -> save(player.getWorld(), player, foodData));
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

        if (!GroupRegister.find(Tuple.of(PerWorldCommands.HUNGERS, from.getName())).map(group -> group.contains(to.getName())).orElse(false)) {
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
        save(world, player, player.getFoodData());
    }

    private void save(World world, Player player, FoodData foodData) {
        if (Bypass.doesBypass(PerWorldCommands.HUNGERS, player)) {
            return;
        }

        GroupRegister.find(Tuple.of(PerWorldCommands.HUNGERS, world.getName()))
                .orElse(Collections.singleton(world.getName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName).flatMap(w -> getHungersPath(w, player)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(path -> HungersSerializer.serialize(path, foodData));
    }

    private void share(World world, Player player) {
        if (Bypass.doesBypass(PerWorldCommands.HUNGERS, player)) {
            return;
        }

        getHungersPath(world, player)
                .flatMap(HungersSerializer::deserialize)
                .ifPresent(player::offer);
    }

    public void onStart() {
        if (!Config.isPersistedOnActivation(PerWorldCommands.HUNGERS.getListenerClass())) {
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

            Optional<Path> optionalPath = getHungersPath(defaultWorldUUID, playerUUID);

            if (!optionalPath.isPresent()) {
                return;
            }

            Path path = optionalPath.get();

            if (Files.exists(path)) {
                return;
            }

            HungersSerializer.serializePlayerData(path, playerDataPath);
        });
    }

}
