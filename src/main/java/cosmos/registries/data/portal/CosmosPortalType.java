package cosmos.registries.data.portal;

import cosmos.registries.portal.CosmosPortal;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.portal.PortalType;

public interface CosmosPortalType extends PortalType {

    BlockType defaultTrigger();

    boolean isAnyOfTriggers(final BlockType trigger);

    <T extends CosmosPortal> Class<T> portalClass();

    <T extends CosmosPortal, B extends CosmosPortal.Builder<T>> Class<B> portalBuilderClass();

}
