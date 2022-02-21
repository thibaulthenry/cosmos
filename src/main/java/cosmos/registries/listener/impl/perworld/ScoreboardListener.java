package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.data.serializable.impl.ScoreboardData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.perworld.GroupRegistry;
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
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.Tuple;

import java.util.Collections;
import java.util.Optional;

@Singleton
public class ScoreboardListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final GroupRegistry groupRegistry;
    private final ScoreboardService scoreboardService;
    private final ScoreboardSerializer serializer;

    @Inject
    public ScoreboardListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
        this.scoreboardService = injector.getInstance(ScoreboardService.class);
        this.serializer = injector.getInstance(ScoreboardSerializer.class);
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.share(event.player().world().key(), event.player());
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        final ResourceKey originalWorldKey = event.originalWorld().key();
        final ResourceKey destinationWorldKey = event.destinationWorld().key();

        if (!this.groupRegistry.find(Tuple.of(PerWorldFeatures.SCOREBOARD, originalWorldKey)).map(group -> group.contains(destinationWorldKey)).orElse(false)) {
            this.share(destinationWorldKey, player);
        }
    }

    @Listener
    public void onRespawnPlayerEvent(final RespawnPlayerEvent.Post event, @First final ServerPlayer player) {
        this.share(event.destinationWorld().key(), event.entity());
    }

    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onPreExecuteCommandEvent(final ExecuteCommandEvent.Pre event) {
        if (!event.command().startsWith("scoreboard") || event.command().startsWith("team")) {
            return;
        }

        final Optional<ResourceKey> worldKey = event.commandCause().location()
                .map(serverLocation -> serverLocation.world().key());

        final String arguments = event.arguments().replaceAll("\\*", " '*' ");

        event.setCommand("cm");
        event.setArguments("scoreboard " + (worldKey.isPresent() ? worldKey.get().formatted() : "") + " " + arguments);
    }

    @Listener
    public void onStoppingServerEvent(final StoppingEngineEvent<Server> event) {
        this.save();
    }

    @Override
    public void save() {
        Sponge.server().worldManager().worldKeys().forEach(this::save);
    }

    private void save(final ResourceKey worldKey) {
        this.save(worldKey, new ScoreboardData(this.scoreboardService.scoreboardOrCreate(worldKey)));
    }

    private void save(final ResourceKey worldKey, final ScoreboardData data) {
        this.groupRegistry.find(Tuple.of(PerWorldFeatures.SCOREBOARD, worldKey))
                .orElse(Collections.singleton(worldKey))
                .stream()
                .map(key -> this.finderService.findCosmosPath(Directories.SCOREBOARDS, key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(path -> this.serializer.serialize(path, data));
    }

    private void share(final ResourceKey worldKey, final ServerPlayer player) {
        player.setScoreboard(this.scoreboardService.scoreboardOrCreate(worldKey));
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
