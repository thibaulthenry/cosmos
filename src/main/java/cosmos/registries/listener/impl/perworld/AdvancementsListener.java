package cosmos.registries.listener.impl.perworld;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.AdvancementTreeData;
import cosmos.registries.listener.ScheduledSaveListener;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.services.perworld.AdvancementsService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class AdvancementsListener extends AbstractListener implements ScheduledSaveListener, ToggleListener {

    @Inject
    private AdvancementsService advancementsService;

    @Inject
    private SerializerProvider serializerProvider;

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.advancementsService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.advancements().serialize(path, new AdvancementTreeData(player)));
        this.advancementsService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.advancements().deserialize(path))
                .ifPresent(data -> data.offer(player));
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                this.advancementsService.getPath(player)
                        .ifPresent(path -> this.serializerProvider.advancements().serialize(path, new AdvancementTreeData(player)))
        );
    }
}
