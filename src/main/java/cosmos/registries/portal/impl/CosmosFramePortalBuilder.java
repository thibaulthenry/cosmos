package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import cosmos.registries.portal.CosmosPortal;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Set;

public class CosmosFramePortalBuilder extends AbstractDataBuilder<CosmosPortal> implements CosmosPortal.Builder {

    private Ticks cooldown;
    private ServerLocation destination;
    private ResourceKey key;
    private Set<ServerLocation> origins;
    private ParticleEffect particleEffect;
    private BlockType trigger;

    @Inject
    public CosmosFramePortalBuilder() {
        this(CosmosPortal.class, 1);
    }

    protected CosmosFramePortalBuilder(final Class<CosmosPortal> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public CosmosPortal.Builder addParticles(final ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;

        return this;
    }

    @Override
    public @NonNull CosmosPortal build() {
        Preconditions.checkNotNull(this.key, "cannot"); // todo
        Preconditions.checkNotNull(this.trigger, "cannot"); // todo
        Preconditions.checkNotNull(this.origins, "cannot"); // todo
        Preconditions.checkArgument(!this.origins.isEmpty(), "cannot"); // todo

        return new CosmosFramePortal(this.key, this.trigger, this.origins, this.destination, this.cooldown, this.particleEffect);
    }

    @Override
    protected Optional<CosmosPortal> buildContent(final DataView container) throws InvalidDataException {
        return Optional.empty();
    }

    @Override
    public CosmosPortal.Builder cooldown(final Ticks ticks) {
        this.cooldown = ticks;

        return this;
    }

    @Override
    public CosmosPortal.Builder destination(final ServerLocation destination) {
        this.destination = destination;

        return this;
    }

    @Override
    public CosmosPortal.Builder from(final CosmosPortal value) {
        Preconditions.checkNotNull(value, "CosmosPortal cannot be null!");
        Preconditions.checkNotNull(value.getKey(), "CosmosPortal key cannot be null!");
        Preconditions.checkNotNull(value.getOrigin(), "CosmosPortal origin cannot be null!");
        Preconditions.checkNotNull(value.getTrigger(), "CosmosPortal trigger cannot be null!");
        this.cooldown = value.getCooldown();
        this.destination = value.getDestination().orElse(null);
        this.key = value.getKey();
        this.origins = value.getOrigins();
        this.particleEffect = value.getParticles();
        this.trigger = value.getTrigger();

        return this;
    }

    @Override
    public CosmosPortal.Builder key(final ResourceKey key) {
        Preconditions.checkNotNull(key, "CosmosPortal key cannot be null!");
        this.key = key;

        return this;
    }

    @Override
    public CosmosPortal.Builder origins(final Set<ServerLocation> origins) {
        this.origins = origins;

        return this;
    }

    @Override
    public CosmosPortal.Builder trigger(final BlockType blockType) {
        Preconditions.checkNotNull(blockType);
        Preconditions.checkArgument(blockType.isAnyOf(BlockTypes.VOID_AIR.get(), BlockTypes.WATER.get(), BlockTypes.LAVA.get(), BlockTypes.NETHER_PORTAL.get()), "can only be "); // todo
        this.trigger = blockType;

        return this;
    }

}
