package cosmos.registries.listener.impl.perworld;


import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.AdvancementTreeData;
import cosmos.registries.listener.ScheduledSaveListener;
import cosmos.registries.serializer.impl.AdvancementsSerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class AdvancementsListener extends AbstractPerWorldListener implements ScheduledSaveListener {

    private final AdvancementsSerializer advancementsSerializer;
    private final FinderService finderService;

    @Inject
    public AdvancementsListener(final Injector injector) {
        this.advancementsSerializer = injector.getInstance(AdvancementsSerializer.class);
        this.finderService = injector.getInstance(FinderService.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.ADVANCEMENTS, event.originalWorld(), player)
                .ifPresent(path -> this.advancementsSerializer.serialize(path, new AdvancementTreeData(player)));

        this.finderService.findCosmosPath(Directories.ADVANCEMENTS, event.destinationWorld(), player)
                .flatMap(this.advancementsSerializer::deserialize)
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers().forEach(player ->
                this.finderService.findCosmosPath(Directories.ADVANCEMENTS, player)
                        .ifPresent(path -> this.advancementsSerializer.serialize(path, new AdvancementTreeData(player)))
        );
    }

}
