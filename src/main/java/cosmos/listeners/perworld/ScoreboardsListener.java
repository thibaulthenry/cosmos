package cosmos.listeners.perworld;

import cosmos.Cosmos;
import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.ScoreboardsSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ScoreboardsListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static final Map<UUID, Scoreboard> computedWorlds = new HashMap<>();

    public static Scoreboard getScoreboard(UUID worldUUID) {
        Optional<World> optionalWorld = Sponge.getServer().getWorld(worldUUID);

        if (!optionalWorld.isPresent()) {
            Cosmos.sendConsole(TextColors.RED, "Impossible to retrieve scoreboard from world uuid : " + worldUUID);
            return Scoreboard.builder().build();
        }

        World world = optionalWorld.get();

        Set<String> group = GroupRegister.find(Tuple.of(PerWorldCommands.SCOREBOARDS, world.getName()))
                .orElseGet(() -> Collections.singleton(world.getName()));

        refreshScoreboard(worldUUID, group);

        return Optional.ofNullable(computedWorlds.get(worldUUID))
                .map(Optional::of)
                .orElseGet(() -> getScoreboardsPath(world)
                        .flatMap(ScoreboardsSerializer::deserialize)
                        .map(scoreboard -> {
                            registerSafely(scoreboard, group);

                            return scoreboard;
                        })
                )
                .orElseGet(() -> {
                    final Scoreboard scoreboard = Scoreboard.builder().build();
                    registerSafely(scoreboard, group);

                    return scoreboard;
                });
    }

    private static Optional<Path> getScoreboardsPath(World world) {
        String fileName = world.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.SCOREBOARDS_DIRECTORY_NAME, fileName);
    }

    @Listener
    public void onCommandSend(SendCommandEvent event, @First CommandSource commandSource) {
        if (!event.getCommand().startsWith("scoreboard")) {
            return;
        }

        if (commandSource instanceof Player && Bypass.doesBypass(PerWorldCommands.SCOREBOARDS, (Player) commandSource)) {
            return;
        }

        String worldName = commandSource instanceof Locatable
                ? ((Locatable) commandSource).getWorld().getName()
                : Sponge.getServer().getDefaultWorldName();

        event.setCommand("cm");
        event.setArguments("scoreboard " + worldName + " " + event.getArguments());
    }

    @Listener
    public void onJoinClientConnectionEvent(ClientConnectionEvent.Join event, @First Player player) {
        share(player.getWorld(), player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (!GroupRegister.find(Tuple.of(PerWorldCommands.SCOREBOARDS, from.getName())).map(group -> group.contains(to.getName())).orElse(false)) {
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

    private static void refreshScoreboard(UUID worldUUID, Set<String> group) {
        Optional.ofNullable(computedWorlds.get(worldUUID))
                .filter(scoreboard -> Collections.frequency(computedWorlds.values(), scoreboard) != group.size())
                .ifPresent(scoreboard ->
                        group.stream()
                                .map(worldName -> Sponge.getServer().getWorld(worldName))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .forEach(world -> computedWorlds.remove(world.getUniqueId()))
                );
    }

    private static void registerSafely(Scoreboard scoreboard, Set<String> group) {
        group.stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(world -> computedWorlds.putIfAbsent(world.getUniqueId(), scoreboard));
    }

    @Override
    public void save() {
        Sponge.getServer().getWorlds().forEach(this::save);
    }

    private void save(World world) {
        save(world, getScoreboard(world.getUniqueId()));
    }

    private void save(World world, Scoreboard scoreboard) {
        GroupRegister.find(Tuple.of(PerWorldCommands.SCOREBOARDS, world.getName()))
                .orElse(Collections.singleton(world.getName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName).flatMap(ScoreboardsListener::getScoreboardsPath))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(path -> ScoreboardsSerializer.serialize(path, scoreboard));
    }

    private static void share(World world, Player player) {
        if (Bypass.doesBypass(PerWorldCommands.SCOREBOARDS, player)) {
            return;
        }

        player.setScoreboard(getScoreboard(world.getUniqueId()));
    }

    @Override
    public void onStart() {
        Sponge.getServer().getOnlinePlayers().forEach(this::onStart);
    }

    @Override
    public void onStart(Player player) {
        share(player.getWorld(), player);
    }

    @Override
    public void onStop() {
        Sponge.getServer().getOnlinePlayers().forEach(this::onStop);
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void onStop(Player player) {
        Scoreboard serverScoreboard = Sponge.getServer().getServerScoreboard().get();

        if (!serverScoreboard.equals(player.getScoreboard())) {
            player.setScoreboard(serverScoreboard);
        }
    }

}
