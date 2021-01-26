package cosmos.registries.listener.impl.perworld;


import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.AdvancementTreeData;
import cosmos.registries.listener.ScheduledSaveListener;
import cosmos.services.perworld.AdvancementsService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class AdvancementsListener extends AbstractPerWorldListener implements ScheduledSaveListener {

    private final AdvancementsService advancementsService;
    private final SerializerProvider serializerProvider;

    @Inject
    public AdvancementsListener(final Injector injector) {
        this.advancementsService = injector.getInstance(AdvancementsService.class);
        this.serializerProvider = injector.getInstance(SerializerProvider.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.advancementsService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.advancements().serialize(path, new AdvancementTreeData(player)));
        this.advancementsService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.advancements().deserialize(path))
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                this.advancementsService.getPath(player)
                        .ifPresent(path -> this.serializerProvider.advancements().serialize(path, new AdvancementTreeData(player)))
        );
    }

}
