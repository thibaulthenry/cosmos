package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.perworld.GroupRegistry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.server.ServerWorld;

@Singleton
public class GameModeListener extends AbstractPerWorldListener {

    private final GroupRegistry groupRegistry;

    @Inject
    public GameModeListener(final Injector injector) {
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
    }

    @Listener
    public void onJoinServerSideConnectionEvent(final ServerSideConnectionEvent.Join event, @First final ServerPlayer player) {
        this.share(player.world(), player);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        final ResourceKey originalWorldKey = event.originalWorld().key();
        final ResourceKey destinationWorldKey = event.destinationWorld().key();

        if (!this.groupRegistry.find(Tuple.of(PerWorldFeatures.GAME_MODE, originalWorldKey)).map(group -> group.contains(destinationWorldKey)).orElse(false)) {
            this.share(event.destinationWorld(), player);
        }
    }

    @Listener
    public void onRespawnPlayerEvent(final RespawnPlayerEvent.Post event, @First final ServerPlayer player) {
        this.share(event.destinationWorld(), event.entity());
    }

    private void share(final ServerWorld world, final ServerPlayer player) {
        player.offer(Keys.GAME_MODE, world.properties().gameMode());
    }

    @Override
    public void start() {
        Sponge.server().onlinePlayers().forEach(player -> this.share(player.world(), player));
    }

}
