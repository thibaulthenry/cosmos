package cosmos.registries.portal;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

public interface CosmosPressurePlatePortal extends CosmosPortal {

    BlockType[] TRIGGERS = {
            BlockTypes.ACACIA_PRESSURE_PLATE.get(),
            BlockTypes.BIRCH_PRESSURE_PLATE.get(),
            BlockTypes.CRIMSON_PRESSURE_PLATE.get(),
            BlockTypes.DARK_OAK_PRESSURE_PLATE.get(),
            BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE.get(),
            BlockTypes.JUNGLE_PRESSURE_PLATE.get(),
            BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE.get(),
            BlockTypes.OAK_PRESSURE_PLATE.get(),
            BlockTypes.POLISHED_BLACKSTONE_PRESSURE_PLATE.get(),
            BlockTypes.SPRUCE_PRESSURE_PLATE.get(),
            BlockTypes.STONE_PRESSURE_PLATE.get(),
            BlockTypes.WARPED_PRESSURE_PLATE.get(),
    };

    static Builder builder() {
        return (Builder) Sponge.game().builderProvider().provide(Builder.class).reset();
    }

    static boolean isAnyOfTriggers(final BlockType trigger) {
        return trigger.isAnyOf(CosmosPressurePlatePortal.TRIGGERS);
    }

    @SuppressWarnings("unchecked")
    default CosmosPortal.Builder<CosmosPressurePlatePortal> asBuilder() {
        return Sponge.game().builderProvider().provide(Builder.class).from(this);
    }

    interface Builder extends CosmosPortal.Builder<CosmosPressurePlatePortal> {
    }

}
