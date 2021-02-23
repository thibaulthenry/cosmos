package cosmos.registries.portal;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Set;

public interface CosmosFramePortal extends CosmosPortal {

    BlockType[] triggerBlocks = {
            BlockTypes.END_PORTAL.get(),
            BlockTypes.LAVA.get(),
            BlockTypes.NETHER_PORTAL.get(),
            BlockTypes.VOID_AIR.get(),
            BlockTypes.WATER.get()
    };

    static boolean isAnyOfTriggerBlocks(final BlockType blockType) {
        return blockType.isAnyOf(CosmosFramePortal.triggerBlocks);
    }

    static CosmosFramePortal.Builder builder() {
        return (CosmosFramePortal.Builder) Sponge.getGame().getBuilderProvider().provide(CosmosFramePortal.Builder.class).reset();
    }

    Set<ServerLocation> getOrigins();

    Optional<ParticleEffect> getParticles();

    BlockType getTrigger();

    boolean isTriggeredBy(BlockType blockType);

    default CosmosFramePortal.Builder asBuilder() {
        return (CosmosFramePortal.Builder) Sponge.getGame().getBuilderProvider().provide(CosmosFramePortal.Builder.class).from(this);
    }

    interface Builder extends CosmosPortal.Builder<CosmosFramePortal> {

        Builder addParticles(ParticleEffect particleEffect);

        Builder origins(Set<ServerLocation> origin);

        Builder trigger(BlockType blockType);

    }

}
