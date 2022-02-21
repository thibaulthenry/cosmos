package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.Cosmos;
import cosmos.constants.DelayFormat;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.ListValue;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PortalTeleportTask {

    private final long delayDurationTicks;
    private final DelayFormat delayFormat;
    private final List<? extends TextColor> delayGradientColors;
    private final Entity entity;
    private final boolean isAudience;
    private final CosmosPortal portal;
    private final Task teleportTask;

    private Task delayTimerTask;
    private Task delayTimerKillTask;
    private Task soundDelayTask;
    private Task soundDelayKillTask;

    private ScheduledTask scheduledDelayTimerTask;
    private ScheduledTask scheduledDelayTimerTaskKill;
    private ScheduledTask scheduledSoundDelayTask;
    private ScheduledTask scheduledSoundDelayTaskKill;
    private ScheduledTask scheduledTeleportTask;

    private long teleportStopTick;

    public PortalTeleportTask(final Entity target, final CosmosPortal portal) {
        final Optional<ServerLocation> optionalDestination = portal.destination();
        Preconditions.checkArgument(optionalDestination.isPresent(), "CosmosPortal has no destination");
        final Ticks delayDuration = portal.delay().orElse(Ticks.zero());
        this.delayDurationTicks = delayDuration.ticks();
        this.delayFormat = portal.delayFormat().orElse(DelayFormat.SECONDS);
        this.delayGradientColors = portal.delayGradientColors().<List<? extends TextColor>>map(ArrayList::new).orElse(null);
        this.entity = target;
        this.isAudience = target instanceof Audience;
        this.portal = portal;
        // final String taskDescription = portal.key().formatted() + "-" + target.uniqueId();

        this.teleportTask = Task.builder()
                .execute(() -> {
                    if (this.isAudience) {
                        portal.soundTravel().ifPresent(((Audience) target)::playSound);
                    }

                    if (this.portal.delay().isPresent()) {
                        this.stopSoundTrigger();
                        this.stopSoundDelay();
                    }

                    Cosmos.services().transportation().teleport(target, optionalDestination.get(), false);
                    Cosmos.services().registry().portalTeleportTask().unregister(target.uniqueId());
                })
                .delay(delayDuration)
                // .name("cosmos-portal-tp-" + taskDescription)
                .plugin(Cosmos.pluginContainer())
                .build();

        if (!(this.isAudience && this.delayDurationTicks > 0)) {
            return;
        }

        final Audience src = (Audience) target;

        if (portal.delayShown().orElse(false)) {
            this.delayTimerTask = Task.builder()
                    .execute(() -> src.sendActionBar(this.formatDelay().color(this.colorDelay())))
                    .interval(this.delayFormat.interval())
                    // .name("cosmos-portal-delay-timer-" + taskDescription)
                    .plugin(Cosmos.pluginContainer())
                    .build();

            this.delayTimerKillTask = Task.builder()
                    .execute(() -> {
                        if (this.scheduledDelayTimerTask != null) {
                            this.scheduledDelayTimerTask.cancel();
                        }

                        src.sendActionBar(Component.empty());
                    })
                    .delay(Ticks.of(this.delayDurationTicks + 1))
                    .plugin(Cosmos.pluginContainer())
                    // .name("cosmos-portal-delay-timer-kill-" + taskDescription)
                    .build();
        }

        final Optional<Sound> optionalSoundDelay = portal.soundDelay();
        final Optional<Ticks> optionalSoundDelayInterval = portal.soundDelayInterval();

        if (optionalSoundDelay.isPresent() && optionalSoundDelayInterval.filter(ticks -> ticks.ticks() > 0).isPresent()) {
            final Sound soundDelay = optionalSoundDelay.get();

            this.soundDelayTask = Task.builder()
                    .execute(() -> {
                        this.stopSoundDelay();
                        src.playSound(soundDelay);
                    })
                    .interval(optionalSoundDelayInterval.get())
                    // .name("cosmos-portal-sound-delay-" + taskDescription)
                    .plugin(Cosmos.pluginContainer())
                    .build();

            this.soundDelayKillTask = Task.builder()
                    .execute(() -> this.cancelTask(this.scheduledSoundDelayTask))
                    .delay(Ticks.of(this.delayDurationTicks - 1))
                    // .name("cosmos-portal-sound-delay-kill-" + taskDescription)
                    .plugin(Cosmos.pluginContainer())
                    .build();
        }
    }

    public void cancel() {
        if (this.portal.delay().isPresent()) {
            this.stopSoundTrigger();
            this.stopSoundDelay();
            this.removeNausea();
        }

        if (this.cancelTask(this.scheduledDelayTimerTask) && this.isAudience) {
            final Audience src = (Audience) this.entity;
            src.sendActionBar(Component.empty());
        }

        this.cancelTask(this.scheduledDelayTimerTaskKill);
        this.cancelTask(this.scheduledSoundDelayTask);
        this.cancelTask(this.scheduledSoundDelayTaskKill);
        this.cancelTask(this.scheduledTeleportTask);
        Cosmos.services().registry().portalTeleportTask().unregister(this.entity.uniqueId());
    }

    private boolean cancelTask(final ScheduledTask scheduledTask) {
        return scheduledTask != null && scheduledTask.cancel();
    }

    public TextColor colorDelay() {
        if (this.delayGradientColors == null || this.delayGradientColors.isEmpty()) {
            return NamedTextColor.WHITE;
        }

        if (this.delayGradientColors.size() == 1) {
            return this.delayGradientColors.get(0);
        }

        double percent = 1.0 - (this.teleportStopTick - Sponge.server().runningTimeTicks().ticks()) / (double) this.delayDurationTicks;
        percent = Math.max(0.0, Math.min(1.0, percent));
        final int rangeNumber = this.delayGradientColors.size() - 1;
        final int nStart = (int) Math.floor(percent * rangeNumber);
        final int nStop = (int) Math.ceil(percent * rangeNumber);
        final TextColor start = this.delayGradientColors.get(nStart);
        final TextColor stop = this.delayGradientColors.get(nStop);

        if (start.compareTo(stop) == 0) {
            return start;
        }

        final double percentRange = 1.0 / rangeNumber;
        final double rangedPercent = (percent - nStart * percentRange) / percentRange;
        int red = (int) (start.red() + rangedPercent * (stop.red() - start.red()));
        int green = (int) (start.green() + rangedPercent * (stop.green() - start.green()));
        int blue = (int) (start.blue() + rangedPercent * (stop.blue() - start.blue()));

        return TextColor.color(red, green, blue);
    }

    public TextComponent formatDelay() {
        long runningTimeTicks = Sponge.server().runningTimeTicks().ticks();

        switch (this.delayFormat) {
            case DIGITAL_CLOCK:
                final long millis = 50 * (this.teleportStopTick - runningTimeTicks);

                final String digits = String.format(
                        "%02d:%02d:%02d.%03d",
                        Math.max(0, TimeUnit.MILLISECONDS.toHours(millis)),
                        Math.max(0, TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1)),
                        Math.max(0, TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)),
                        Math.max(0, millis % TimeUnit.SECONDS.toMillis(1))
                );

                return Component.text(digits);
            case PERCENTAGE:
                final double percent = 100 * ((this.teleportStopTick - runningTimeTicks) / (double) this.delayDurationTicks);
                return Component.text(Math.max(0, Math.min(100, 100 - Math.round(percent))) + "%");
            case SECONDS:
                final long ticks = Math.max(0, this.teleportStopTick - runningTimeTicks);
                return Component.text(TimeUnit.MILLISECONDS.toSeconds(50 * ticks));
            case TICKS:
                return Component.text(Math.max(0, this.teleportStopTick - runningTimeTicks));
            default:
                return Component.empty();
        }
    }

    public boolean isType(final PortalType portalType) {
        return this.portal.type().equals(portalType);
    }

    private void removeNausea() {
        if (!this.portal.nausea().orElse(false)) {
            return;
        }

        this.entity.getValue(Keys.POTION_EFFECTS)
                .map(ListValue::asMutable)
                .map(potionEffects -> potionEffects.removeAll(potionEffect -> PotionEffectTypes.NAUSEA.get().equals(potionEffect.type())))
                .ifPresent(this.entity::tryOffer);
    }

    public void stopSoundDelay() {
        this.portal.soundDelay()
                .filter(sound -> this.isAudience)
                .map(sound -> SoundStop.namedOnSource(sound.name(), sound.source()))
                .ifPresent(((Audience) this.entity)::stopSound);
    }

    public void stopSoundTrigger() {
        this.portal.soundTrigger()
                .filter(sound -> this.isAudience)
                .map(sound -> SoundStop.namedOnSource(sound.name(), sound.source()))
                .ifPresent(((Audience) this.entity)::stopSound);
    }

    public void submit() {
        if (this.isAudience && this.delayDurationTicks > 0) {
            this.portal.soundTrigger().ifPresent(((Audience) this.entity)::playSound);

            if (this.portal.nausea().orElse(false)) {
                final ListValue<PotionEffect> potionEffects = this.entity.getValue(Keys.POTION_EFFECTS)
                        .map(ListValue::asMutable)
                        .orElse(ListValue.mutableOf(Keys.POTION_EFFECTS, new LinkedList<>()))
                        .add(
                                PotionEffect.builder()
                                        .ambient(false)
                                        .duration(Ticks.of(this.delayDurationTicks + 60))
                                        .potionType(PotionEffectTypes.NAUSEA)
                                        .showIcon(false)
                                        .showParticles(false)
                                        .build()
                        );

                this.entity.tryOffer(potionEffects);
            }
        }

        this.teleportStopTick = Sponge.server().runningTimeTicks().ticks() + this.delayDurationTicks;
        this.scheduledTeleportTask = Sponge.server().scheduler().submit(this.teleportTask);

        if (this.delayTimerTask != null) {
            this.scheduledDelayTimerTask = Sponge.asyncScheduler().submit(this.delayTimerTask);
            this.scheduledDelayTimerTaskKill = Sponge.asyncScheduler().submit(this.delayTimerKillTask);
        }

        if (this.soundDelayTask != null) {
            this.scheduledSoundDelayTask = Sponge.asyncScheduler().submit(this.soundDelayTask);
            this.scheduledSoundDelayTaskKill = Sponge.asyncScheduler().submit(this.soundDelayKillTask);
        }
    }

}
