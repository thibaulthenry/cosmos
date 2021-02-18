package cosmos.services.portal;

import com.google.inject.ImplementedBy;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosPortal;
import cosmos.services.CosmosService;
import cosmos.services.portal.impl.PortalServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collection;

@ImplementedBy(PortalServiceImpl.class)
public interface PortalService extends CosmosService {

    CosmosPortal create(Audience src, ResourceKey key, CosmosPortalType type) throws CommandException;

    void delete(Audience src, ResourceKey key) throws CommandException;

    void fill(Audience src, CosmosPortal portal) throws CommandException;

    void fill(Audience src, CosmosPortal portal, BlockState blockState) throws CommandException;

    void fill(Audience src, CosmosPortal portal, BlockEntity blockEntity) throws CommandException;

    void highlight(BlockType blockType, Ticks duration, String tag, Collection<? extends ServerLocation> locations);

    void highlight(BlockType blockType, Ticks duration, String tag, ServerLocation... locations);

    void link(Audience src, ResourceKey worldOriginKey, PortalType portalType, ResourceKey worldDestinationKey) throws CommandException;

    void unlink(Audience src, ResourceKey worldOriginKey, PortalType portalType) throws CommandException;

}
