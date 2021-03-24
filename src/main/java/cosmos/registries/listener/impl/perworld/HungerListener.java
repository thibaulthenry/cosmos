package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.HungerData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.serializer.impl.HungerSerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class HungerListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final HungerSerializer hungerSerializer;

    @Inject
    public HungerListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.hungerSerializer = injector.getInstance(HungerSerializer.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.HUNGERS, event.originalWorld(), player)
                .ifPresent(path -> this.hungerSerializer.serialize(path, new HungerData(player)));

        this.finderService.findCosmosPath(Directories.HUNGERS, event.destinationWorld(), player)
                .flatMap(this.hungerSerializer::deserialize)
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers().forEach(player ->
                this.finderService.findCosmosPath(Directories.HUNGERS, player)
                        .ifPresent(path -> this.hungerSerializer.serialize(path, new HungerData(player)))
        );
    }

}
