package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.HealthData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.services.perworld.HealthsService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class HealthsListener extends AbstractListener implements ScheduledAsyncSaveListener, ToggleListener {

    @Inject
    private HealthsService healthsService;

    @Inject
    private SerializerProvider serializerProvider;

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.healthsService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.healths().serialize(path, new HealthData(player)));
        this.healthsService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.healths().deserialize(path))
                .ifPresent(data -> data.offer(player));
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                this.healthsService.getPath(player)
                        .ifPresent(path -> this.serializerProvider.healths().serialize(path, new HealthData(player)))
        );
    }


}
