package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.registries.portal.CosmosPortal;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.portal.PortalTypes;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Set;

public class CosmosFramePortal implements CosmosPortal {

    private final Ticks cooldown;
    private final ServerLocation destination;
    private final ResourceKey key;
    private final Set<ServerLocation> origins;
    private final ParticleEffect particleEffect;
    private final BlockType trigger;
    private final PortalType type;

    public CosmosFramePortal(final ResourceKey key, final BlockType trigger, final Set<ServerLocation> origins,
                             @Nullable final ServerLocation destination, @Nullable final Ticks cooldown,
                             @Nullable final ParticleEffect particleEffect) {
        Preconditions.checkNotNull(key, "cannot"); // todo
        Preconditions.checkNotNull(trigger, "cannot"); // todo
        Preconditions.checkNotNull(origins, "cannot"); // todo
        Preconditions.checkArgument(!origins.isEmpty(), "cannot"); // todo

        this.cooldown = cooldown;
        this.destination = destination;
        this.key = key;
        this.origins = origins;
        this.particleEffect = particleEffect;
        this.trigger = trigger;
        this.type = PortalTypes.END.get(); // todo
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
    public ParticleEffect getParticles() {
        return this.particleEffect;
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
    public boolean isTriggeredBy(final BlockType blockType) {
        return this.trigger != null && this.trigger.isAnyOf(blockType);
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew(); // todo
    }

}
