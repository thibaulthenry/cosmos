package cosmos.registries.portal;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

public interface CosmosFramePortal extends CosmosPortal {

    BlockType[] triggers = {
            BlockTypes.END_PORTAL.get(),
            BlockTypes.LAVA.get(),
            BlockTypes.NETHER_PORTAL.get(),
            BlockTypes.VOID_AIR.get(),
            BlockTypes.WATER.get()
    };

    static Builder builder() {
        return (Builder) Sponge.getGame().getBuilderProvider().provide(Builder.class).reset();
    }

    static boolean isAnyOfTriggers(final BlockType trigger) {
        return trigger.isAnyOf(CosmosFramePortal.triggers);
    }

    @SuppressWarnings("unchecked")
    default CosmosPortal.Builder<CosmosFramePortal> asBuilder() {
        return Sponge.getGame().getBuilderProvider().provide(Builder.class).from(this);
    }

    interface Builder extends CosmosPortal.Builder<CosmosFramePortal> {
    }

}
