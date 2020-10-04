package cosmos.listeners.perworld;

import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.finders.FinderWorldProperties;
import cosmos.statics.serializers.ScoreboardsSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ScoreboardsListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static final Map<UUID, Scoreboard> computedWorlds = new HashMap<>();

    public static Scoreboard getScoreboard(UUID worldUUID) {
        if (computedWorlds.containsKey(worldUUID)) {
            return computedWorlds.get(worldUUID);
        }

        Scoreboard scoreboard = getScoreboardsPath(worldUUID)
                .flatMap(ScoreboardsSerializer::deserialize)
                .orElse(Scoreboard.builder().build());

        computedWorlds.putIfAbsent(worldUUID, scoreboard);

        return scoreboard;
    }

    private static Optional<Path> getScoreboardsPath(UUID worldUUID) {
        String fileName = worldUUID + ".dat";
        return FinderFile.getCosmosPath(FinderFile.SCOREBOARDS_DIRECTORY_NAME, fileName);
    }

    @Listener
    public void onCommandSend(SendCommandEvent event, @First CommandSource commandSource) {
        if (!event.getCommand().startsWith("scoreboard")) {
            return;
        }

        String worldName = commandSource instanceof Locatable ?
                ((Locatable) commandSource).getWorld().getName() :
                Sponge.getServer().getDefaultWorldName();

        event.setCommand("cm");
        event.setArguments("scoreboard " + worldName + " " + event.getArguments());
    }

    @Listener
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        player.setScoreboard(getScoreboard(to.getUniqueId()));
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @First Player player) {
        player.setScoreboard(getScoreboard(player.getWorld().getUniqueId()));
    }

    @Listener
    public void onGamePostInitialization(GameStartingServerEvent event) {
        FinderWorldProperties.getAllWorldProperties(true)
                .stream()
                .map(WorldProperties::getUniqueId)
                .forEach(uuid -> getScoreboardsPath(uuid)
                        .flatMap(ScoreboardsSerializer::deserialize)
                        .ifPresent(scoreboard -> computedWorlds.putIfAbsent(uuid, scoreboard))
                );
    }

    @Listener
    public void onGameStoppingEvent(GameStoppingEvent event) {
        save();
    }

    @Override
    public void onStart() {
        Sponge.getServer().getOnlinePlayers().forEach(player -> player.setScoreboard(getScoreboard(player.getWorld().getUniqueId())));
    }

    @Override
    public void onStop() {
        Sponge.getServer().getServerScoreboard().ifPresent(scoreboard ->
                Sponge.getServer().getOnlinePlayers().forEach(player -> player.setScoreboard(scoreboard))
        );
    }

    @Override
    public void save() {
        computedWorlds.forEach(((uuid, scoreboard) -> getScoreboardsPath(uuid)
                .ifPresent(path -> ScoreboardsSerializer.serialize(path, scoreboard)))
        );
    }
}
