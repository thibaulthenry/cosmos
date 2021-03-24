package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.ScoreboardData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.perworld.ScoreboardRegistry;
import cosmos.registries.serializer.impl.ScoreboardSerializer;
import cosmos.services.io.FinderService;
import cosmos.services.perworld.ScoreboardService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.util.Tristate;

import java.util.Optional;

@Singleton
public class ScoreboardListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final ScoreboardRegistry scoreboardRegistry;
    private final ScoreboardSerializer scoreboardSerializer;
    private final ScoreboardService scoreboardService;

    @Inject
    public ScoreboardListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.scoreboardRegistry = injector.getInstance(ScoreboardRegistry.class);
        this.scoreboardSerializer = injector.getInstance(ScoreboardSerializer.class);
        this.scoreboardService = injector.getInstance(ScoreboardService.class);
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        player.setScoreboard(this.scoreboardService.scoreboardOrCreate(player.world()));
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        player.setScoreboard(this.scoreboardService.scoreboardOrCreate(event.destinationWorld()));
    }

    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onPreExecuteCommandEvent(final ExecuteCommandEvent.Pre event) {
        if (!event.command().startsWith("scoreboard")) { // TODO Manage /team commands
            return;
        }

        final Optional<ResourceKey> worldKey = event.commandCause().location()
                .map(serverLocation -> serverLocation.world().key());

        event.setCommand("cm");
        event.setArguments("scoreboard " + (worldKey.isPresent() ? worldKey.get().formatted() : "") + " " + event.arguments());
    }

    @Listener
    public void onStoppingServerEvent(final StoppingEngineEvent<Server> event) {
        this.save();
    }

    @Override
    public void save() {
        this.scoreboardRegistry.streamEntries().forEach(entry ->
                this.finderService.findCosmosPath(Directories.SCOREBOARDS, entry.key()).ifPresent(path ->
                        this.scoreboardSerializer.serialize(path, new ScoreboardData(entry.value()))
                )
        );
    }

    @Override
    public void start() {
        Sponge.server().onlinePlayers().forEach(player ->
                player.setScoreboard(this.scoreboardService.scoreboardOrCreate(player.world()))
        );
    }

    @Override
    public void stop() {
        Sponge.server().serverScoreboard().ifPresent(scoreboard ->
                Sponge.server().onlinePlayers().forEach(player -> player.setScoreboard(scoreboard))
        );
    }

}
