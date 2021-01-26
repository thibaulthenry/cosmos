package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.HungerData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.services.perworld.HungersService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class HungersListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final HungersService hungersService;
    private final SerializerProvider serializerProvider;

    @Inject
    public HungersListener(final Injector injector) {
        this.hungersService = injector.getInstance(HungersService.class);
        this.serializerProvider = injector.getInstance(SerializerProvider.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.hungersService.getPath(event.getOriginalWorld(), player)
                .ifPresent(path -> this.serializerProvider.hungers().serialize(path, new HungerData(player)));
        this.hungersService.getPath(event.getDestinationWorld(), player)
                .flatMap(path -> this.serializerProvider.hungers().deserialize(path))
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers().forEach(player ->
                this.hungersService.getPath(player)
                        .ifPresent(path -> this.serializerProvider.hungers().serialize(path, new HungerData(player)))
        );
    }

}
