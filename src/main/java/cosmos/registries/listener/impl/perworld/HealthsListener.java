package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.HealthData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.services.perworld.HealthsService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class HealthsListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final HealthsService healthsService;
    private final SerializerProvider serializerProvider;

    @Inject
    public HealthsListener(final Injector injector) {
        this.healthsService = injector.getInstance(HealthsService.class);
        this.serializerProvider = injector.getInstance(SerializerProvider.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.healthsService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.healths().serialize(path, new HealthData(player)));
        this.healthsService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.healths().deserialize(path))
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                this.healthsService.getPath(player)
                        .ifPresent(path -> this.serializerProvider.healths().serialize(path, new HealthData(player)))
        );
    }

}
