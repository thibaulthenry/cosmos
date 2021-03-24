package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.registries.data.serializable.impl.ExperienceData;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.serializer.impl.ExperienceSerializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class ExperienceListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private final ExperienceSerializer experienceSerializer;
    private final FinderService finderService;

    @Inject
    public ExperienceListener(final Injector injector) {
        this.experienceSerializer = injector.getInstance(ExperienceSerializer.class);
        this.finderService = injector.getInstance(FinderService.class);
    }

    @Listener
    public void onPostChangeEntityWorldEvent(final ChangeEntityWorldEvent.Post event, @First final ServerPlayer player) {
        this.finderService.findCosmosPath(Directories.EXPERIENCES, event.originalWorld(), player)
                .ifPresent(path -> this.experienceSerializer.serialize(path, new ExperienceData(player)));

        this.finderService.findCosmosPath(Directories.EXPERIENCES, event.destinationWorld(), player)
                .flatMap(this.experienceSerializer::deserialize)
                .ifPresent(data -> data.share(player));
    }

    @Override
    public void save() {
        Sponge.server().onlinePlayers().forEach(player ->
                this.finderService.findCosmosPath(Directories.EXPERIENCES, player)
                        .ifPresent(path -> this.experienceSerializer.serialize(path, new ExperienceData(player)))
        );
    }

}
