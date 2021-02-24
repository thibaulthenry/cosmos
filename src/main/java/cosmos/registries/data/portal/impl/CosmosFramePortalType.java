package cosmos.registries.data.portal.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.PortalRegistry;
import cosmos.services.transportation.TransportationService;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Axis;
import org.spongepowered.api.world.portal.Portal;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;

@Singleton
public class CosmosFramePortalType implements CosmosPortalType {

    private final PortalRegistry portalRegistry;
    private final TransportationService transportationService;

    @Inject
    public CosmosFramePortalType(final Injector injector) {
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CosmosPortal> CosmosPortal.Builder<T> builder() {
        return (CosmosPortal.Builder<T>) CosmosFramePortal.builder();
    }

    @Override
    public BlockType defaultTrigger() {
        return BlockTypes.VOID_AIR.get();
    }

    @Override
    public Optional<Portal> findPortal(final ServerLocation location) {
        return Optional.ofNullable(this.portalRegistry.value(location.asLocatableBlock()));
    }

    @Override
    public void generatePortal(final ServerLocation location, final Axis axis) {

    }

    @Override
    public boolean teleport(final Entity entity, final ServerLocation destination, boolean generateDestinationPortal) {
        return this.findPortal(entity.getServerLocation())
                .flatMap(Portal::getDestination)
                .map(portalDestination -> this.transportationService.teleport(entity, portalDestination, false))
                .orElse(false);
    }

}
