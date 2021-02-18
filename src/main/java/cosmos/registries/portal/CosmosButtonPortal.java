package cosmos.registries.portal;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

public interface CosmosButtonPortal extends CosmosPortal {

    BlockType[] TRIGGERS = {
            BlockTypes.ACACIA_BUTTON.get(),
            BlockTypes.BIRCH_BUTTON.get(),
            BlockTypes.CRIMSON_BUTTON.get(),
            BlockTypes.DARK_OAK_BUTTON.get(),
            BlockTypes.JUNGLE_BUTTON.get(),
            BlockTypes.OAK_BUTTON.get(),
            BlockTypes.POLISHED_BLACKSTONE_BUTTON.get(),
            BlockTypes.SPRUCE_BUTTON.get(),
            BlockTypes.STONE_BUTTON.get(),
            BlockTypes.WARPED_BUTTON.get(),
    };

    static Builder builder() {
        return (Builder) Sponge.game().builderProvider().provide(Builder.class).reset();
    }

    static boolean isAnyOfTriggers(final BlockType trigger) {
        return trigger.isAnyOf(CosmosButtonPortal.TRIGGERS);
    }

    @SuppressWarnings("unchecked")
    default CosmosPortal.Builder<CosmosButtonPortal> asBuilder() {
        return Sponge.game().builderProvider().provide(Builder.class).from(this);
    }

    interface Builder extends CosmosPortal.Builder<CosmosButtonPortal> {
    }

}
