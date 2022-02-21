package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.Cosmos;
import cosmos.registries.portal.CosmosPortal;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;

import java.util.Random;

public class PortalSoundAmbientTask {

    private static final Random RANDOM = new Random();

    private final Task soundAmbientTask;

    private ScheduledTask scheduledSoundAmbientTask;

    public PortalSoundAmbientTask(final CosmosPortal portal) {
        Preconditions.checkArgument(portal.soundAmbient().isPresent(), "CosmosPortal has no sound ambient");

        this.soundAmbientTask = Task.builder()
                .execute(() -> portal.origins().forEach(location ->
                        portal.soundAmbient()
                                .filter(sound -> RANDOM.nextInt(15 * portal.originsSize()) == 0)
                                .ifPresent(sound -> location.world().playSound(sound, location.position()))
                ))
                .interval(Ticks.of(20))
                // .name("cosmos-portal-sound-ambient-" + portal.key().formatted())
                .plugin(Cosmos.pluginContainer())
                .build();
    }

    public void cancel() {
        this.cancelTask(this.scheduledSoundAmbientTask);
    }

    private boolean cancelTask(final ScheduledTask scheduledTask) {
        return scheduledTask != null && scheduledTask.cancel();
    }

    public void submit() {
        if (this.soundAmbientTask != null) {
            this.scheduledSoundAmbientTask = Sponge.asyncScheduler().submit(this.soundAmbientTask);
        }
    }

}
