package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.Cosmos;
import cosmos.constants.DelayFormat;
import cosmos.constants.Queries;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractCosmosPortalBuilder<T extends CosmosPortal> extends AbstractDataBuilder<T> implements CosmosPortal.Builder<T> {

    protected Ticks delay;
    protected DelayFormat delayFormat;
    protected Collection<? extends TextColor> delayGradientColors;
    protected Boolean delayShown;
    protected ServerLocation destination;
    protected ResourceKey key;
    protected Boolean nausea;
    protected Set<ServerLocation> origins = new HashSet<>();
    protected ParticleEffect particles;
    protected Boolean particlesFluctuation;
    protected Ticks particlesSpawnInterval;
    protected Integer particlesViewDistance;
    protected Sound soundAmbient;
    protected Sound soundDelay;
    protected Ticks soundDelayInterval;
    protected Sound soundTravel;
    protected Sound soundTrigger;
    protected BlockType trigger;

    protected AbstractCosmosPortalBuilder(final Class<T> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public CosmosPortal.Builder<T> addOrigin(final ServerLocation origin) {
        Preconditions.checkNotNull(origin, "CosmosPortal origins cannot be null");
        this.origins = Collections.singleton(origin);
        return this;
    }

    @Override
    protected Optional<T> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Portal.KEY, Queries.Portal.ORIGINS, Queries.Portal.TRIGGER, Queries.Portal.TYPE)) {
            return Optional.empty();
        }

        final ResourceKey key = container.getResourceKey(Queries.Portal.KEY)
                .orElseThrow(() -> new InvalidDataException("Missing key while building CosmosPortal"));

        final Set<ServerLocation> origins = new HashSet<>(
                container.getSerializableList(Queries.Portal.ORIGINS, ServerLocation.class)
                        .orElseThrow(() -> new InvalidDataException("Missing origins while building CosmosPortal"))
        );

        final BlockType trigger = container.getRegistryValue(Queries.Portal.TRIGGER, RegistryTypes.BLOCK_TYPE)
                .orElseThrow(() -> new InvalidDataException("Missing trigger while building CosmosPortal"));

        final CosmosPortalType type = container.getRegistryValue(Queries.Portal.TYPE, RegistryTypes.PORTAL_TYPE)
                .filter(portalType -> portalType instanceof CosmosPortalType)
                .map(portalType -> (CosmosPortalType) portalType)
                .orElseThrow(() -> new InvalidDataException("Missing type while building CosmosPortal"));

        final CosmosPortal.Builder<T> builder = CosmosPortal.builder(type.portalBuilderClass());

        try {
            builder.key(key).origins(origins).trigger(trigger);

            container.getLong(Queries.Portal.DELAY)
                    .filter(value -> value > 0)
                    .map(Ticks::of)
                    .ifPresent(builder::delay);

            container.getResourceKey(Queries.Portal.DELAY_FORMAT)
                    .flatMap(DelayFormat::fromKey)
                    .ifPresent(builder::delayFormat);

            container.getIntegerList(Queries.Portal.DELAY_GRADIENT_COLORS)
                    .map(value -> value.stream().map(NamedTextColor::namedColor).collect(Collectors.toList()))
                    .ifPresent(builder::delayGradientColors);

            container.getBoolean(Queries.Portal.DELAY_SHOWN).ifPresent(builder::delayShown);

            container.getSerializable(Queries.Portal.DESTINATION, ServerLocation.class)
                    .ifPresent(builder::destination);

            container.getBoolean(Queries.Portal.NAUSEA).ifPresent(builder::nausea);

            container.getView(Queries.Portal.PARTICLES)
                    .flatMap(view -> ParticleEffect.builder().build(view))
                    .ifPresent(builder::particles);

            container.getBoolean(Queries.Portal.PARTICLES_FLUCTUATION).ifPresent(builder::particlesFluctuation);

            container.getLong(Queries.Portal.PARTICLES_SPAWN_INTERVAL)
                    .filter(value -> value > 0)
                    .map(Ticks::of)
                    .ifPresent(builder::particlesSpawnInterval);

            container.getView(Queries.Portal.SOUND_AMBIENT)
                    .flatMap(this::buildSound)
                    .ifPresent(builder::soundAmbient);

            container.getView(Queries.Portal.SOUND_DELAY)
                    .flatMap(this::buildSound)
                    .ifPresent(builder::soundDelay);

            container.getLong(Queries.Portal.SOUND_DELAY_INTERVAL)
                    .filter(value -> value > 0)
                    .map(Ticks::of)
                    .ifPresent(builder::soundDelayInterval);

            container.getView(Queries.Portal.SOUND_TRAVEL)
                    .flatMap(this::buildSound)
                    .ifPresent(builder::soundTravel);

            container.getView(Queries.Portal.SOUND_TRIGGER)
                    .flatMap(this::buildSound)
                    .ifPresent(builder::soundTrigger);
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while building CosmosPortal content", e);
            return Optional.empty();
        }

        return Optional.of(builder.build());
    }

    protected Optional<Sound> buildSound(final DataView container) {
        if (!container.contains(Queries.Portal.Sound.PITCH, Queries.Portal.Sound.TYPE, Queries.Portal.Sound.VOLUME)) {
            return Optional.empty();
        }

        final float pitch = container.getFloat(Queries.Portal.Sound.PITCH)
                .orElseThrow(() -> new InvalidDataException("Missing pitch while building Sound"));

        final SoundType type = container.getRegistryValue(Queries.Portal.Sound.TYPE, RegistryTypes.SOUND_TYPE)
                .orElseThrow(() -> new InvalidDataException("Missing type while building CosmosPortal"));

        final float volume = container.getFloat(Queries.Portal.Sound.VOLUME)
                .orElseThrow(() -> new InvalidDataException("Missing volume while building Sound"));

        return Optional.of(Sound.sound(type, Sound.Source.BLOCK, volume, pitch));
    }

    @Override
    public CosmosPortal.Builder<T> delay(final Ticks delay) {
        Preconditions.checkArgument(delay == null || delay.ticks() > 0, "CosmosPortal delay cannot be zero");
        this.delay = delay;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> delayFormat(final DelayFormat delayFormat) {
        this.delayFormat = delayFormat;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> delayGradientColors(final Collection<? extends TextColor> delayGradientColors) {
        this.delayGradientColors = delayGradientColors;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> delayShown(final Boolean delayShown) {
        this.delayShown = delayShown;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> destination(final ServerLocation destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> from(final T portal) {
        Preconditions.checkNotNull(portal, "CosmosPortal cannot be null!");
        Preconditions.checkNotNull(portal.key(), "CosmosPortal key cannot be null");
        Preconditions.checkNotNull(portal.trigger(), "CosmosPortal trigger cannot be null");
        Preconditions.checkNotNull(portal.origins(), "CosmosPortal origins cannot be null");
        Preconditions.checkArgument(!portal.origins().isEmpty(), "CosmosPortal origins cannot be empty");

        portal.delay().ifPresent(value -> this.delay = value);
        portal.delayFormat().ifPresent(value -> this.delayFormat = value);
        portal.delayGradientColors().ifPresent(value -> this.delayGradientColors = value);
        portal.delayShown().ifPresent(value -> this.delayShown = value);
        portal.destination().ifPresent(value -> this.destination = value);
        this.key = portal.key();
        portal.nausea().ifPresent(value -> this.nausea = value);
        this.origins = portal.origins();
        portal.particles().ifPresent(value -> this.particles = value);
        portal.particlesFluctuation().ifPresent(value -> this.particlesFluctuation = value);
        portal.particlesSpawnInterval().ifPresent(value -> this.particlesSpawnInterval = value);
        portal.particlesViewDistance().ifPresent(value -> this.particlesViewDistance = value);
        portal.soundAmbient().ifPresent(value -> this.soundAmbient = value);
        portal.soundDelay().ifPresent(value -> this.soundDelay = value);
        portal.soundDelayInterval().ifPresent(value -> this.soundDelayInterval = value);
        portal.soundTravel().ifPresent(value -> this.soundTravel = value);
        portal.soundTrigger().ifPresent(value -> this.soundTrigger = value);
        this.trigger = portal.trigger();
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> key(final ResourceKey key) {
        Preconditions.checkNotNull(key, "CosmosPortal key cannot be null!");
        this.key = key;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> nausea(final Boolean nausea) {
        this.nausea = nausea;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> origins(final Set<ServerLocation> origins) {
        Preconditions.checkNotNull(origins, "CosmosPortal origins cannot be null");
        Preconditions.checkArgument(!origins.isEmpty(), "CosmosPortal origins cannot be empty");
        this.origins = new HashSet<>(origins);
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particles(final ParticleEffect particles) {
        this.particles = particles;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particlesFluctuation(final Boolean particlesFluctuation) {
        this.particlesFluctuation = particlesFluctuation;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particlesSpawnInterval(final Ticks particlesSpawnInterval) {
        Preconditions.checkArgument(particlesSpawnInterval == null || particlesSpawnInterval.ticks() > 0, "CosmosPortal particles spawn interval cannot be zero");
        this.particlesSpawnInterval = particlesSpawnInterval;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particlesViewDistance(final Integer particlesViewDistance) {
        Preconditions.checkArgument(particlesViewDistance == null || particlesViewDistance > 0, "CosmosPortal particles view distance cannot be zero");
        this.particlesViewDistance = particlesViewDistance;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundAmbient(final Sound soundAmbient) {
        this.soundAmbient = soundAmbient;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundDelay(final Sound soundDelay) {
        this.soundDelay = soundDelay;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundDelayInterval(final Ticks soundDelayInterval) {
        this.soundDelayInterval = soundDelayInterval;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundTravel(final Sound soundTravel) {
        this.soundTravel = soundTravel;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundTrigger(final Sound soundTrigger) {
        this.soundTrigger = soundTrigger;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> trigger(final BlockType trigger) {
        Preconditions.checkNotNull(trigger);
        Preconditions.checkArgument(this.isAnyOfTriggers(trigger), trigger + " is not available in the trigger list");
        this.trigger = trigger;
        return this;
    }

    protected abstract boolean isAnyOfTriggers(BlockType trigger);

}
