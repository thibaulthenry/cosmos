package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.Cosmos;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.PortalType;

import java.util.Optional;

public class PortalTeleportTask {

    private final long delayTicks;
    private final Entity entity;
    private final PortalType portalType;
    private final Task remainingTickTask;
    private final Task remainingTickKillTask;
    private final Task soundDelayTask;
    private final Task soundDelayKillTask;
    private final Task teleportTask;
    private ScheduledTask scheduledRemainingTickTask;
    private ScheduledTask scheduledRemainingTickKillTask;
    private ScheduledTask scheduledSoundDelayTask;
    private ScheduledTask scheduledSoundDelayKillTask;
    private ScheduledTask scheduledTeleportTask;
    private long teleportExecutionTick;

    public PortalTeleportTask(final Entity target, final CosmosPortal portal) {
        Preconditions.checkArgument(portal.getDestination().isPresent(), "CosmosPortal has no destination");

        this.entity = target;
        this.portalType = portal.type();

        final Optional<Ticks> optionalDelay = portal.delay().filter(delay -> delay.getTicks() > 0);

        this.delayTicks = optionalDelay.map(Ticks::getTicks).orElse(0L);

        final Optional<Sound> optionalSoundDelay = portal.soundDelay();
        final Optional<Ticks> optionalSoundDelayInterval = Optional.of(portal.soundDelayInterval()).filter(interval -> interval.getTicks() > 0);

        this.teleportTask = Task.builder()
                .execute(() -> {
                    if (target instanceof Audience) {
                        portal.soundTravel().ifPresent(((Audience) target)::playSound);
                    }

                    Cosmos.getServices().transportation().teleport(target, portal.getDestination().get(), false);
                    Cosmos.getRegistries().portalTeleportTask().unregister(target.getUniqueId());
                })
                .delay(optionalDelay.orElse(Ticks.zero()))
                .plugin(Cosmos.getPluginContainer())
                .build();

        if (!(optionalDelay.isPresent() && target instanceof Audience)) {
            this.remainingTickTask = null;
            this.remainingTickKillTask = null;
            this.soundDelayTask = null;
            this.soundDelayKillTask = null;

            return;
        }

        final Audience src = (Audience) target;

        if (target instanceof Audience) {

            this.remainingTickTask = Task.builder()
                    .execute(() -> src.sendActionBar(Component.text(this.teleportExecutionTick - Sponge.getServer().getRunningTimeTicks())))
                    .interval(Ticks.single())
                    .plugin(Cosmos.getPluginContainer())
                    .build();

            this.remainingTickKillTask = Task.builder()
                    .execute(() -> {
                        if (this.scheduledRemainingTickTask != null) {
                            this.scheduledRemainingTickTask.cancel();
                        }

                        src.sendActionBar(Component.text(0));
                        src.sendActionBar(Component.empty());
                    })
                    .delay(optionalDelay.get())
                    .plugin(Cosmos.getPluginContainer())
                    .build();
        } else {
            this.remainingTickTask = null;
            this.remainingTickKillTask = null;
        }

        if (optionalSoundDelay.isPresent() && optionalSoundDelayInterval.isPresent()) {
            this.soundDelayTask = Task.builder()
                    .execute(() -> ((Audience) target).playSound(optionalSoundDelay.get()))
                    .interval(optionalSoundDelayInterval.get())
                    .plugin(Cosmos.getPluginContainer())
                    .build();

            this.soundDelayKillTask = Task.builder()
                    .execute(() -> {
                        if (this.scheduledSoundDelayTask != null) {
                            this.scheduledSoundDelayTask.cancel();
                        }
                    })
                    .delay(optionalDelay.get())
                    .plugin(Cosmos.getPluginContainer())
                    .build();
        } else {
            this.soundDelayTask = null;
            this.soundDelayKillTask = null;
        }
    }

    public void cancel() {
        if (this.scheduledRemainingTickTask != null) {
            this.scheduledRemainingTickTask.cancel();

            if (this.entity instanceof Audience) {
                final Audience src = (Audience) this.entity;
                src.sendActionBar(Component.text(0));
                src.sendActionBar(Component.empty());
            }
        }

        if (this.scheduledRemainingTickKillTask != null) {
            this.scheduledRemainingTickKillTask.cancel();
        }

        if (this.scheduledSoundDelayTask != null) {
            this.scheduledSoundDelayTask.cancel();
        }

        if (this.scheduledSoundDelayKillTask != null) {
            this.scheduledSoundDelayKillTask.cancel();
        }

        if (this.scheduledTeleportTask != null) {
            this.scheduledTeleportTask.cancel();
        }

        Cosmos.getRegistries().portalTeleportTask().unregister(this.entity.getUniqueId());
    }

    public boolean isType(final PortalType portalType) {
        return this.portalType.equals(portalType);
    }

    public void submit() {
        this.teleportExecutionTick = Sponge.getServer().getRunningTimeTicks() + this.delayTicks;
        this.scheduledTeleportTask = Sponge.getServer().getScheduler().submit(this.teleportTask);

        if (this.remainingTickTask != null) {
            this.scheduledRemainingTickTask = Sponge.getAsyncScheduler().submit(this.remainingTickTask);
            this.scheduledRemainingTickKillTask = Sponge.getAsyncScheduler().submit(this.remainingTickKillTask);
        }

        if (this.soundDelayTask != null) {
            this.scheduledSoundDelayTask = Sponge.getAsyncScheduler().submit(this.soundDelayTask);
            this.scheduledSoundDelayKillTask = Sponge.getAsyncScheduler().submit(this.soundDelayKillTask);
        }
    }


}
