package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.sound.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CosmosFramePortalBuilderImpl extends AbstractDataBuilder<CosmosFramePortal> implements CosmosFramePortal.Builder {

    private Ticks cooldown;
    private ServerLocation destination;
    private ResourceKey key;
    private boolean nausea;
    private Set<ServerLocation> origins;
    private ParticleEffect particleEffect;
    private Sound soundAmbiance;
    private Sound soundTravel;
    private Sound soundTrigger;
    private BlockType trigger;

    @Inject
    public CosmosFramePortalBuilderImpl() {
        this(CosmosFramePortal.class, 1);
    }

    protected CosmosFramePortalBuilderImpl(final Class<CosmosFramePortal> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public CosmosFramePortal.Builder addParticles(final ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
        return this;
    }

    @Override
    public @NonNull CosmosFramePortal build() {
        Preconditions.checkNotNull(this.key, "CosmosPortal cannot be null!");
        Preconditions.checkNotNull(this.trigger, "CosmosPortal key cannot be null");
        Preconditions.checkNotNull(this.origins, "CosmosPortal trigger cannot be null");
        Preconditions.checkArgument(!this.origins.isEmpty(), "CosmosPortal origins cannot be null");

        return new CosmosFramePortalImpl(
                this.key, this.trigger, this.origins, this.nausea,
                this.cooldown, this.destination, this.particleEffect,
                this.soundAmbiance, this.soundTravel, this.soundTrigger
        );
    }

    @Override
    protected Optional<CosmosFramePortal> buildContent(final DataView container) throws InvalidDataException {
        return Optional.empty();
    }

    @Override
    public CosmosFramePortal.Builder cooldown(final Ticks ticks) {
        this.cooldown = ticks;
        return this;
    }

    @Override
    public CosmosFramePortal.Builder destination(final ServerLocation destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public CosmosFramePortal.Builder from(final CosmosFramePortal value) {
        Preconditions.checkNotNull(value, "CosmosPortal cannot be null!");
        Preconditions.checkNotNull(value.getKey(), "CosmosPortal key cannot be null");
        Preconditions.checkNotNull(value.getTrigger(), "CosmosPortal trigger cannot be null");
        Preconditions.checkNotNull(value.getOrigin(), "CosmosPortal origins cannot be null");

        this.cooldown = value.getCooldown();
        this.destination = value.getDestination().orElse(null);
        this.key = value.getKey();
        this.nausea = value.hasNausea();
        this.origins = value.getOrigins();
        this.particleEffect = value.getParticles().orElse(null);
        this.trigger = value.getTrigger();

        return this;
    }

    @Override
    public CosmosFramePortal.Builder key(final ResourceKey key) {
        Preconditions.checkNotNull(key, "CosmosPortal key cannot be null!");
        this.key = key;
        return this;
    }

    @Override
    public CosmosFramePortal.Builder nausea(final boolean state) {
        this.nausea = state;
        return this;
    }

    @Override
    public CosmosFramePortal.Builder origin(final ServerLocation origin) {
        this.origins = Collections.singleton(origin);
        return this;
    }

    @Override
    public CosmosPortal.Builder<CosmosFramePortal> soundAmbiance(final Sound sound) {
        this.soundAmbiance = sound;
        return this;
    }

    @Override
    public CosmosPortal.Builder<CosmosFramePortal> soundTravel(final Sound sound) {
        this.soundTravel = sound;
        return this;
    }

    @Override
    public CosmosPortal.Builder<CosmosFramePortal> soundTrigger(final Sound sound) {
        this.soundTrigger = sound;
        return this;
    }

    @Override
    public CosmosFramePortal.Builder origins(final Set<ServerLocation> origins) {
        this.origins = new HashSet<>(origins);
        return this;
    }

    @Override
    public CosmosFramePortal.Builder trigger(final BlockType blockType) {
        Preconditions.checkNotNull(blockType);
        Preconditions.checkArgument(CosmosFramePortal.isAnyOfTriggerBlocks(blockType), blockType + " is not available in the trigger list");
        this.trigger = blockType;
        return this;
    }

}
