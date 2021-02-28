package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.sound.Sound;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Set;

abstract class AbstractCosmosPortal implements CosmosPortal {

    protected final Ticks delay;
    protected final ServerLocation destination;
    protected final ResourceKey key;
    protected final boolean nausea;
    protected final Set<ServerLocation> origins;
    protected final int originsSize;
    protected final ParticleEffect particles;
    protected final Ticks particlesInterval;
    protected final Sound soundAmbiance;
    protected final Sound soundDelay;
    protected final Ticks soundDelayInterval;
    protected final Sound soundTravel;
    protected final Sound soundTrigger;
    protected final BlockType trigger;
    protected final PortalType type;

    AbstractCosmosPortal(final ResourceKey key, final Set<ServerLocation> origins,
                         final BlockType trigger, final PortalType type,
                         @Nullable final Ticks delay, @Nullable final ServerLocation destination,
                         @Nullable final Boolean nausea, @Nullable final ParticleEffect particles,
                         @Nullable final Ticks particlesInterval, @Nullable final Sound soundAmbiance,
                         @Nullable final Sound soundDelay, @Nullable final Ticks soundDelayInterval,
                         @Nullable final Sound soundTravel, @Nullable final Sound soundTrigger) {
        Preconditions.checkNotNull(key, "CosmosFramePortal key cannot be null");
        Preconditions.checkNotNull(origins, "CosmosFramePortal origins cannot be null");
        Preconditions.checkArgument(!origins.isEmpty(), "CosmosFramePortal origins cannot be empty");
        Preconditions.checkNotNull(trigger, "CosmosFramePortal trigger cannot be null");
        Preconditions.checkNotNull(type, "CosmosFramePortal trigger cannot be null");

        this.delay = delay;
        this.destination = destination;
        this.key = key;
        this.nausea = Optional.ofNullable(nausea).orElse(false);
        this.origins = origins;
        this.originsSize = origins.size();
        this.particles = particles;
        this.particlesInterval = Optional.ofNullable(particlesInterval).orElse(Ticks.single());
        this.soundAmbiance = soundAmbiance;
        this.soundDelay = soundDelay;
        this.soundDelayInterval = Optional.ofNullable(soundDelayInterval).orElse(Ticks.single());
        this.soundTravel = soundTravel;
        this.soundTrigger = soundTrigger;
        this.trigger = trigger;
        this.type = type;
    }

    @Override
    public Optional<Ticks> delay() {
        return Optional.ofNullable(this.delay);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public Optional<ServerLocation> getDestination() {
        return Optional.ofNullable(this.destination);
    }

    @Override
    public ServerLocation getOrigin() {
        return this.origins.toArray(new ServerLocation[0])[0];
    }

    @Override
    public PortalType getType() {
        return this.type;
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
    public boolean nausea() {
        return this.nausea;
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
    public Ticks particlesInterval() {
        return this.particlesInterval;
    }

    @Override
    public Optional<Sound> soundAmbiance() {
        return Optional.ofNullable(this.soundAmbiance);
    }

    @Override
    public Optional<Sound> soundDelay() {
        return Optional.ofNullable(this.soundDelay);
    }

    @Override
    public Ticks soundDelayInterval() {
        return this.soundDelayInterval;
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
        return DataContainer.createNew(); // todo
    }

    @Override
    public BlockType trigger() {
        return this.trigger;
    }

    @Override
    public PortalType type() {
        return this.type;
    }

}
