package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.constants.DelayFormat;
import cosmos.constants.Queries;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractCosmosPortal implements CosmosPortal {

    protected final Ticks delay;
    protected final DelayFormat delayFormat;
    protected final Collection<? extends TextColor> delayGradientColors;
    protected final Boolean delayShown;
    protected final ServerLocation destination;
    protected final ResourceKey key;
    protected final Boolean nausea;
    protected final Set<ServerLocation> origins;
    protected final int originsSize;
    protected final ParticleEffect particles;
    protected final Boolean particlesFluctuation;
    protected final Ticks particlesSpawnInterval;
    protected final Integer particlesViewDistance;
    protected final Sound soundAmbient;
    protected final Sound soundDelay;
    protected final Ticks soundDelayInterval;
    protected final Sound soundTravel;
    protected final Sound soundTrigger;
    protected final BlockType trigger;
    protected final CosmosPortalType type;

    AbstractCosmosPortal(final ResourceKey key, final Set<ServerLocation> origins,
                         final BlockType trigger, final CosmosPortalType type,
                         @Nullable final Ticks delay, @Nullable final DelayFormat delayFormat,
                         @Nullable final Collection<? extends TextColor> delayGradientColors,
                         @Nullable final Boolean delayShown, @Nullable final ServerLocation destination,
                         @Nullable final Boolean nausea, @Nullable final ParticleEffect particles,
                         @Nullable final Boolean particlesFluctuation, @Nullable final Ticks particlesSpawnInterval,
                         @Nullable final Integer particlesViewDistance, @Nullable final Sound soundAmbient,
                         @Nullable final Sound soundDelay, @Nullable final Ticks soundDelayInterval,
                         @Nullable final Sound soundTravel, @Nullable final Sound soundTrigger) {
        Preconditions.checkNotNull(key, "CosmosPortal key cannot be null");
        Preconditions.checkNotNull(origins, "CosmosPortal origins cannot be null");
        Preconditions.checkArgument(!origins.isEmpty(), "CosmosPortal origins cannot be empty");
        Preconditions.checkNotNull(trigger, "CosmosPortal trigger cannot be null");
        Preconditions.checkNotNull(type, "CosmosPortal type cannot be null");
        Preconditions.checkArgument(particlesSpawnInterval == null || particlesSpawnInterval.ticks() > 0, "CosmosPortal particles spawn interval cannot be zero");
        Preconditions.checkArgument(soundDelayInterval == null || soundDelayInterval.ticks() > 0, "CosmosPortal sound delay interval cannot be zero");

        this.delay = delay;
        this.delayFormat = delayFormat;
        this.delayGradientColors = delayGradientColors;
        this.delayShown = delayShown;
        this.destination = destination;
        this.key = key;
        this.nausea = nausea;
        this.origins = origins;
        this.originsSize = origins.size();
        this.particles = particles;
        this.particlesFluctuation = particlesFluctuation;
        this.particlesSpawnInterval = particlesSpawnInterval;
        this.particlesViewDistance = particlesViewDistance;
        this.soundAmbient = soundAmbient;
        this.soundDelay = soundDelay;
        this.soundDelayInterval = soundDelayInterval;
        this.soundTravel = soundTravel;
        this.soundTrigger = soundTrigger;
        this.trigger = trigger;
        this.type = type;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public Optional<Ticks> delay() {
        return Optional.ofNullable(this.delay).filter(delay -> delay.ticks() > 0);
    }

    @Override
    public Optional<DelayFormat> delayFormat() {
        return Optional.ofNullable(this.delayFormat);
    }

    @Override
    public Optional<Collection<? extends TextColor>> delayGradientColors() {
        return Optional.ofNullable(this.delayGradientColors);
    }

    @Override
    public Optional<Boolean> delayShown() {
        return Optional.ofNullable(this.delayShown);
    }

    @Override
    public Optional<ServerLocation> destination() {
        return Optional.ofNullable(this.destination);
    }

    @Override
    public boolean isTriggeredBy(final BlockType blockType) {
        return this.trigger != null && this.trigger.isAnyOf(blockType);
    }

    @Override
    public ResourceKey key() {
        return this.key;
    }

    @Override
    public Optional<Boolean> nausea() {
        return Optional.ofNullable(this.nausea);
    }

    @Override
    public ServerLocation origin() {
        return this.origins.toArray(new ServerLocation[0])[0];
    }

    @Override
    public Set<ServerLocation> origins() {
        return this.origins;
    }

    @Override
    public int originsSize() {
        return this.originsSize;
    }

    @Override
    public Optional<ParticleEffect> particles() {
        return Optional.ofNullable(this.particles);
    }

    @Override
    public Optional<Boolean> particlesFluctuation() {
        return Optional.ofNullable(this.particlesFluctuation);
    }

    @Override
    public Optional<Ticks> particlesSpawnInterval() {
        return Optional.ofNullable(this.particlesSpawnInterval);
    }

    @Override
    public Optional<Integer> particlesViewDistance() {
        return Optional.ofNullable(this.particlesViewDistance);
    }

    @Override
    public Optional<Sound> soundAmbient() {
        return Optional.ofNullable(this.soundAmbient);
    }

    @Override
    public Optional<Sound> soundDelay() {
        return Optional.ofNullable(this.soundDelay);
    }

    @Override
    public Optional<Ticks> soundDelayInterval() {
        return Optional.ofNullable(this.soundDelayInterval);
    }

    @Override
    public Optional<Sound> soundTravel() {
        return Optional.ofNullable(this.soundTravel);
    }

    @Override
    public Optional<Sound> soundTrigger() {
        return Optional.ofNullable(this.soundTrigger);
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer dataContainer = DataContainer.createNew()
                .set(Queries.Portal.KEY, this.key)
                .set(Queries.Portal.ORIGINS, this.origins.stream().map(ServerLocation::toContainer).collect(Collectors.toList()))
                .set(Queries.Portal.TRIGGER, this.trigger.key(RegistryTypes.BLOCK_TYPE))
                .set(Queries.Portal.TYPE, this.type.key(RegistryTypes.PORTAL_TYPE));

        Optional.ofNullable(this.delay)
                .map(Ticks::ticks)
                .ifPresent(value -> dataContainer.set(Queries.Portal.DELAY, value));

        Optional.ofNullable(this.delayFormat)
                .map(DelayFormat::key)
                .ifPresent(value -> dataContainer.set(Queries.Portal.DELAY_FORMAT, value));

        Optional.ofNullable(this.delayGradientColors)
                .map(value -> value.stream().map(TextColor::value).collect(Collectors.toList()))
                .ifPresent(value -> dataContainer.set(Queries.Portal.DELAY_GRADIENT_COLORS, value));

        Optional.ofNullable(this.delayShown)
                .ifPresent(value -> dataContainer.set(Queries.Portal.DELAY_SHOWN, value));

        Optional.ofNullable(this.destination)
                .map(ServerLocation::toContainer)
                .ifPresent(value -> dataContainer.set(Queries.Portal.DESTINATION, value));

        Optional.ofNullable(this.nausea)
                .ifPresent(value -> dataContainer.set(Queries.Portal.NAUSEA, value));

        Optional.ofNullable(this.particles)
                .ifPresent(value -> dataContainer.set(Queries.Portal.PARTICLES, value));

        Optional.ofNullable(this.particlesFluctuation)
                .ifPresent(value -> dataContainer.set(Queries.Portal.PARTICLES_FLUCTUATION, value));

        Optional.ofNullable(this.particlesSpawnInterval)
                .map(Ticks::ticks)
                .ifPresent(value -> dataContainer.set(Queries.Portal.PARTICLES_SPAWN_INTERVAL, value));

        Optional.ofNullable(this.particlesViewDistance)
                .ifPresent(value -> dataContainer.set(Queries.Portal.PARTICLES_VIEW_DISTANCE, value));

        Optional.ofNullable(this.soundAmbient)
                .map(this::toContainerSound)
                .ifPresent(value -> dataContainer.set(Queries.Portal.SOUND_AMBIENT, value));

        Optional.ofNullable(this.soundDelay)
                .map(this::toContainerSound)
                .ifPresent(value -> dataContainer.set(Queries.Portal.SOUND_DELAY, value));

        Optional.ofNullable(this.soundDelayInterval)
                .map(Ticks::ticks)
                .ifPresent(value -> dataContainer.set(Queries.Portal.SOUND_DELAY_INTERVAL, value));

        Optional.ofNullable(this.soundTravel)
                .map(this::toContainerSound)
                .ifPresent(value -> dataContainer.set(Queries.Portal.SOUND_TRAVEL, value));

        Optional.ofNullable(this.soundTrigger)
                .map(this::toContainerSound)
                .ifPresent(value -> dataContainer.set(Queries.Portal.SOUND_TRIGGER, value));

        return dataContainer;
    }

    private DataContainer toContainerSound(final Sound sound) {
        return DataContainer.createNew()
                .set(Queries.Portal.Sound.PITCH, sound.pitch())
                .set(Queries.Portal.Sound.TYPE, sound.name().asString())
                .set(Queries.Portal.Sound.VOLUME, sound.volume());
    }

    @Override
    public BlockType trigger() {
        return this.trigger;
    }

    @Override
    public CosmosPortalType type() {
        return this.type;
    }

}
