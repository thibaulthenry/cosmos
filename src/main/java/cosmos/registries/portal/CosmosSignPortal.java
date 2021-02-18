package cosmos.registries.portal;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

public interface CosmosSignPortal extends CosmosPortal {

    BlockType[] TRIGGERS = {
            BlockTypes.ACACIA_SIGN.get(),
            BlockTypes.ACACIA_WALL_SIGN.get(),
            BlockTypes.BIRCH_SIGN.get(),
            BlockTypes.BIRCH_WALL_SIGN.get(),
            BlockTypes.CRIMSON_SIGN.get(),
            BlockTypes.CRIMSON_WALL_SIGN.get(),
            BlockTypes.DARK_OAK_SIGN.get(),
            BlockTypes.DARK_OAK_WALL_SIGN.get(),
            BlockTypes.JUNGLE_SIGN.get(),
            BlockTypes.JUNGLE_WALL_SIGN.get(),
            BlockTypes.OAK_SIGN.get(),
            BlockTypes.OAK_WALL_SIGN.get(),
            BlockTypes.SPRUCE_SIGN.get(),
            BlockTypes.SPRUCE_WALL_SIGN.get(),
            BlockTypes.WARPED_SIGN.get(),
            BlockTypes.WARPED_WALL_SIGN.get(),
    };

    static Builder builder() {
        return (Builder) Sponge.game().builderProvider().provide(Builder.class).reset();
    }

    static boolean isAnyOfTriggers(final BlockType trigger) {
        return trigger.isAnyOf(CosmosSignPortal.TRIGGERS);
    }

    @SuppressWarnings("unchecked")
    default CosmosPortal.Builder<CosmosSignPortal> asBuilder() {
        return Sponge.game().builderProvider().provide(Builder.class).from(this);
    }

    interface Builder extends CosmosPortal.Builder<CosmosSignPortal> {
    }

}
