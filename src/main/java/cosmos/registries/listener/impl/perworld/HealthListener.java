package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.HealthData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.serializer.impl.HealthSerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class HealthListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final FinderService finderService;
    private final HealthSerializer healthSerializer;

    @Inject
    public HealthListener(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.healthSerializer = injector.getInstance(HealthSerializer.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.HEALTHS, event.originalWorld(), player)
                .ifPresent(path -> this.healthSerializer.serialize(path, new HealthData(player)));

        this.finderService.findCosmosPath(Directories.HEALTHS, event.destinationWorld(), player)
                .flatMap(this.healthSerializer::deserialize)
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers().forEach(player ->
                this.finderService.findCosmosPath(Directories.HEALTHS, player)
                        .ifPresent(path -> this.healthSerializer.serialize(path, new HealthData(player)))
        );
    }

}
