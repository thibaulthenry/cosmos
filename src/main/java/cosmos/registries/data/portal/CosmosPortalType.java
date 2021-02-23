package cosmos.registries.data.portal;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.portal.PortalType;

public interface CosmosPortalType extends PortalType {

    BlockType defaultTrigger();

}
