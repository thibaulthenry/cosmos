package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.registries.portal.CosmosFramePortal;
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
    protected long particlesInterval;
    protected Sound soundAmbiance;
    protected Sound soundTravel;
    protected Sound soundTrigger;
    protected BlockType trigger;

    protected AbstractCosmosPortalBuilder(final Class<T> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
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
    public CosmosPortal.Builder<T> addOrigin(final ServerLocation origin) {
        this.origins = Collections.singleton(origin);
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particles(final ParticleEffect particles) {
        this.particles = particles;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> particlesInterval(long particlesInterval) {
        this.particlesInterval = particlesInterval;
        return this;
    }

    @Override
    public CosmosPortal.Builder<T> soundAmbiance(final Sound soundAmbiance) {
        this.soundAmbiance = soundAmbiance;
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
        Preconditions.checkArgument(CosmosFramePortal.isAnyOfTriggerBlocks(trigger), trigger + " is not available in the trigger list");
        this.trigger = trigger;
        return this;
    }

}
