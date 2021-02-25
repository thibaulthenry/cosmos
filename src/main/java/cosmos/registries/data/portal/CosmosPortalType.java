package cosmos.registries.data.portal;

import cosmos.registries.portal.CosmosPortal;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.portal.PortalType;

public interface CosmosPortalType extends PortalType {

    <T extends CosmosPortal> CosmosPortal.Builder<T> asPortalBuilder();

    BlockType defaultTrigger();

}
