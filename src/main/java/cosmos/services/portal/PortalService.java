package cosmos.services.portal;

import com.google.inject.ImplementedBy;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.impl.PortalDispatcher;
import cosmos.services.CosmosService;
import cosmos.services.portal.impl.PortalServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.util.Axis;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@ImplementedBy(PortalServiceImpl.class)
public interface PortalService extends CosmosService {

    void create(Audience src, ResourceKey key, BlockType triggerBlockType) throws CommandException;

    void highlight(Ticks duration, String tag, ServerLocation... serverLocation);

    void highlight(Audience src, Ticks duration) throws CommandException;

    boolean link(ResourceKey worldOriginKey, PortalType portalType, ResourceKey worldDestinationKey) throws CommandException;

    boolean unlink(ResourceKey worldOriginKey, PortalType portalType);

    boolean unselect(Audience src) throws CommandException;

}
