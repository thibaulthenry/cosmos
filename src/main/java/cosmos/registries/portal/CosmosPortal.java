package cosmos.registries.portal;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.CopyableBuilder;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.Portal;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Set;

public interface CosmosPortal extends DataSerializable, Portal {

    static Builder builder() {
        return Sponge.getGame().getBuilderProvider().provide(Builder.class).reset();
    }

    Ticks getCooldown();

    ResourceKey getKey();

    Set<ServerLocation> getOrigins();

    ParticleEffect getParticles();

    BlockType getTrigger();

    PortalType getType();

    boolean hasCooldown();

    boolean isTriggeredBy(BlockType blockType);

    default Builder asBuilder() {
        return Sponge.getGame().getBuilderProvider().provide(Builder.class).from(this);
    }

    interface Builder extends org.spongepowered.api.util.Builder<CosmosPortal, Builder>, CopyableBuilder<CosmosPortal, Builder> {

        Builder addParticles(ParticleEffect particleEffect);

        Builder cooldown(Ticks ticks);

        Builder destination(ServerLocation destination);

        Builder key(ResourceKey key);

        Builder origins(Set<ServerLocation> origin);

        Builder trigger(BlockType blockType);

    }

}
