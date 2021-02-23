package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.constants.CosmosPortalTypes;
import cosmos.registries.portal.CosmosFramePortal;
import net.kyori.adventure.sound.Sound;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class CosmosFramePortalImpl implements CosmosFramePortal {

    private final Ticks cooldown;
    private final ServerLocation destination;
    private final ResourceKey key;
    private final boolean nausea;
    private final Set<ServerLocation> origins;
    private final ParticleEffect particleEffect;
    private final long particleInterval = 1;
    private final Sound soundAmbiance;
    private final Sound soundTravel;
    private final Sound soundTrigger;
    private final BlockType trigger;
    private final PortalType type;

    public CosmosFramePortalImpl(final ResourceKey key, final BlockType trigger, final Set<ServerLocation> origins, final boolean nausea,
                                 @Nullable final Ticks cooldown, @Nullable final ServerLocation destination,
                                 @Nullable final ParticleEffect particleEffect, @Nullable final Sound soundAmbiance,
                                 @Nullable final Sound soundTravel, @Nullable final Sound soundTrigger) {
        Preconditions.checkNotNull(key, "CosmosFramePortal key cannot be null");
        Preconditions.checkNotNull(trigger, "CosmosFramePortal trigger cannot be null");
        Preconditions.checkNotNull(origins, "CosmosFramePortal origins cannot be null");
        Preconditions.checkArgument(!origins.isEmpty(), "CosmosFramePortal origins cannot be empty");

        this.cooldown = cooldown;
        this.destination = destination;
        this.key = key;
        this.nausea = nausea;
        this.origins = origins;
        this.particleEffect = particleEffect;
        this.trigger = trigger;
        this.soundAmbiance = Optional.ofNullable(soundAmbiance)
                .orElse(Sound.sound(SoundTypes.BLOCK_PORTAL_AMBIENT.get(), Sound.Source.BLOCK, 0.09f, 1f));
        this.soundTravel = Optional.ofNullable(soundTravel)
                .orElse(Sound.sound(SoundTypes.BLOCK_PORTAL_TRAVEL.get(), Sound.Source.BLOCK, 0.8f, 1f));
        this.soundTrigger = Optional.ofNullable(soundTrigger)
                .orElse(Sound.sound(SoundTypes.BLOCK_PORTAL_TRIGGER.get(), Sound.Source.BLOCK, 0.1f, 1f));
        this.type = CosmosPortalTypes.FRAME.get();
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public Ticks getCooldown() {
        return this.cooldown;
    }

    @Override
    public Optional<ServerLocation> getDestination() {
        return Optional.ofNullable(this.destination);
    }

    @Override
    public ResourceKey getKey() {
        return this.key;
    }

    @Override
    public Set<ServerLocation> getOrigins() {
        return this.origins;
    }

    @Override
    public Optional<ParticleEffect> getParticles() {
        return Optional.ofNullable(this.particleEffect);
    }

    @Override
    public BlockType getTrigger() {
        return this.trigger;
    }

    @Override
    public PortalType getType() {
        return this.type;
    }

    @Override
    public ServerLocation getOrigin() {
        return this.origins.toArray(new ServerLocation[0])[0];
    }

    @Override
    public boolean hasCooldown() {
        return this.cooldown != null;
    }

    @Override
    public boolean hasNausea() {
        return this.nausea;
    }

    @Override
    public Sound soundAmbiance() {
        return this.soundAmbiance;
    }

    @Override
    public Sound soundTravel() {
        return this.soundTravel;
    }

    @Override
    public Sound soundTrigger() {
        return this.soundTrigger;
    }

    @Override
    public boolean isTriggeredBy(final BlockType blockType) {
        return this.trigger != null && this.trigger.isAnyOf(blockType);
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew(); // todo
    }

}
