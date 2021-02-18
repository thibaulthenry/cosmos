package cosmos.registries.portal.impl;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PortalDispatcher {

    private final Map<PortalType, ResourceKey> portalLinks = new HashMap<>();

    public boolean addLink(final PortalType portalType, final ResourceKey worldDestinationKey) {
        return this.portalLinks.put(portalType, worldDestinationKey) != null;
    }

    public Optional<ResourceKey> findLink(final PortalType portalType) {
        return Optional.ofNullable(this.portalLinks.get(portalType));
    }

    public Optional<ServerWorld> findLinkedWorld(final PortalType portalType) {
        return this.findLink(portalType).flatMap(worldKey -> Sponge.server().worldManager().world(worldKey));
    }

    public boolean removeLink(final PortalType portalType) {
        return this.portalLinks.remove(portalType) != null;
    }

}
