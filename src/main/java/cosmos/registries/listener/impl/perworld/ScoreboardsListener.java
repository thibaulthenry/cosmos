package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.services.perworld.ScoreboardsService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

import java.util.Optional;

@Singleton
public class ScoreboardsListener extends AbstractListener implements ScheduledAsyncSaveListener, ToggleListener {

    @Inject
    private ScoreboardsService scoreboardsService;

    @Inject
    private SerializerProvider serializerProvider;

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        player.setScoreboard(this.scoreboardsService.getOrCreateScoreboard(player.getWorld()));
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        player.setScoreboard(this.scoreboardsService.getOrCreateScoreboard(event.getDestinationWorld()));
    }

    @Listener
    public void onPreExecuteCommandEvent(final ExecuteCommandEvent.Pre event, @First final CommandCause cause) {
        if (!event.getCommand().startsWith("scoreboard")) {
            return;
        }

        final Optional<ResourceKey> worldKey = cause.getLocation()
                .map(serverLocation -> serverLocation.getWorld().getKey());

        event.setCommand("cm");
        event.setArguments("scoreboard " + (worldKey.isPresent() ? worldKey.get().getFormatted() : "") + " " + event.getArguments());
    }

    @Listener
    public void onStoppingServerEvent(final StoppingEngineEvent<Server> event) {
        this.save();
    }

    @Override
    public void save() {
//        this.scoreboardsService.getScoreboards().forEach(scoreboard -> todo
//                this.scoreboardsService.getPath(uuid)
//                        .ifPresent(path -> this.serializerProvider.scoreboards().serialize(path, new ScoreboardData(scoreboard)))
//        );
    }

    @Override
    public void start() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                player.setScoreboard(this.scoreboardsService.getOrCreateScoreboard(player.getWorld()))
        );
    }

    @Override
    public void stop() {
        Sponge.getServer().getServerScoreboard().ifPresent(scoreboard ->
                Sponge.getServer().getOnlinePlayers().forEach(player -> player.setScoreboard(scoreboard))
        );
    }
}
