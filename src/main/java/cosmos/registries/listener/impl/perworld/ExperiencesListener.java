package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.ExperienceData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.services.perworld.ExperiencesService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class ExperiencesListener extends AbstractListener implements ScheduledAsyncSaveListener, ToggleListener {

    @Inject
    private ExperiencesService experiencesService;

    @Inject
    private SerializerProvider serializerProvider;

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.experiencesService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.experiences().serialize(path, new ExperienceData(player)));
        this.experiencesService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.experiences().deserialize(path))
                .ifPresent(data -> data.offer(player));
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                this.experiencesService.getPath(player)
                        .ifPresent(path -> this.serializerProvider.experiences().serialize(path, new ExperienceData(player)))
        );
    }
}
