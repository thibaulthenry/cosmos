package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractCosmosPortalBuilder<T extends CosmosPortal> extends AbstractDataBuilder<T> implements CosmosPortal.Builder<T> {

    protected Ticks delay;
    protected ServerLocation destination;
    protected ResourceKey key;
    protected boolean nausea;
    protected Set<ServerLocation> origins = new HashSet<>();
    protected ParticleEffect particles;
    protected Ticks particlesInterval;
    protected Sound soundAmbiance;
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
        this.origins = Collections.singleton(origin);
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> delay(final Ticks delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> destination(final ServerLocation destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> from(final T value) {
        Preconditions.checkNotNull(value, "CosmosPressurePlatePortal cannot be null!");
        Preconditions.checkNotNull(value.key(), "CosmosPressurePlatePortal key cannot be null");
        Preconditions.checkNotNull(value.trigger(), "CosmosPressurePlatePortal trigger cannot be null");
        Preconditions.checkNotNull(value.getOrigin(), "CosmosPressurePlatePortal origins cannot be null");

        this.delay = value.delay().orElse(null);
        this.destination = value.getDestination().orElse(null);
        this.key = value.key();
        this.nausea = value.nausea();
        this.origins = value.origins();
        this.particles = value.particles().orElse(null);
        this.particlesInterval = value.particlesInterval();
        this.soundAmbiance = value.soundAmbiance().orElse(null);
        this.soundTravel = value.soundTravel().orElse(null);
        this.soundTrigger = value.soundTrigger().orElse(null);
        this.trigger = value.trigger();

        return this;
    }

    @Override
    public CosmosPortal.Builder<T> key(final ResourceKey key) {
        Preconditions.checkNotNull(key, "CosmosPortal key cannot be null!");
        this.key = key;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> nausea(final boolean nausea) {
        this.nausea = nausea;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particles(final ParticleEffect particles) {
        this.particles = particles;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particlesInterval(Ticks particlesInterval) {
        this.particlesInterval = particlesInterval;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundAmbiance(final Sound soundAmbiance) {
        this.soundAmbiance = soundAmbiance;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundDelay(final Sound soundDelay) {
        this.soundDelay = soundDelay;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundDelayInterval(Ticks soundDelayInterval) {
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
    public CosmosPortal.Builder<T> origins(final Set<ServerLocation> origins) {
        this.origins = new HashSet<>(origins);
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
