package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.HungerData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.serializer.impl.HungersSerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class HungersListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final HungersSerializer hungersSerializer;

    @Inject
    public HungersListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.hungersSerializer = injector.getInstance(HungersSerializer.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.HUNGERS, event.originalWorld(), player)
                .ifPresent(path -> this.hungersSerializer.serialize(path, new HungerData(player)));

        this.finderService.findCosmosPath(Directories.HUNGERS, event.destinationWorld(), player)
                .flatMap(this.hungersSerializer::deserialize)
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers().forEach(player ->
                this.finderService.findCosmosPath(Directories.HUNGERS, player)
                        .ifPresent(path -> this.hungersSerializer.serialize(path, new HungerData(player)))
        );
    }

}
