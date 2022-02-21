package cosmos.services.transportation.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.PortalTeleportTaskRegistry;
import cosmos.registries.portal.impl.PortalTeleportTask;
import cosmos.services.transportation.TransportationService;
import cosmos.services.validation.ValidationService;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TransportationServiceImpl implements TransportationService {

    private final PortalTeleportTaskRegistry portalTeleportTaskRegistry;
    private final ValidationService validationService;

    @Inject
    public TransportationServiceImpl(final Injector injector) {
        this.portalTeleportTaskRegistry = injector.getInstance(PortalTeleportTaskRegistry.class);
        this.validationService = injector.getInstance(ValidationService.class);
    }

    @Override
    public String buildCommand(@Nullable final String target, @Nullable final ResourceKey worldKey,
                               @Nullable final Vector3d position, @Nullable final Vector3d rotation, final boolean safeOnly) {
        return MessageFormat.format(
                "/cm move{0}{1}{2}{3}{4}",
                safeOnly ? " --safe-only" : "",
                target == null ? "" : " " + target,
                worldKey == null ? "" : " " + worldKey.formatted(),
                position == null ? "" : " " + position.x() + " " + position.y() + " " + position.z(),
                rotation == null ? "" : " " + rotation.x() + " " + rotation.y() + " " + rotation.z()
        );
    }

    @Override
    public boolean mustNotify(final Audience src, final Identifiable target) {
        if (!(src instanceof Identifiable) || !(target instanceof Audience)) {
            return false;
        }

        return !this.validationService.isSelf(src, target);
    }

    @Override
    public boolean teleport(final Entity target, final CosmosPortal portal) {
        final Optional<ServerLocation> optionalDestination = portal.destination();

        if (!optionalDestination.isPresent()) {
            return false;
        }

        final UUID targetUUID = target.uniqueId();

        if (this.portalTeleportTaskRegistry.has(targetUUID)) {
            return false;
        }

        final PortalTeleportTask portalTeleportTask = new PortalTeleportTask(target, portal);
        portalTeleportTask.submit();

        this.portalTeleportTaskRegistry.register(targetUUID, portalTeleportTask);

        return true;
    }

    @Override
    public boolean teleport(final Entity target, final ServerLocation location, @Nullable final Vector3d rotation, final boolean safeOnly) {
        Sponge.server().causeStackManager().pushCause(Cosmos.pluginContainer());

        if (rotation != null) {
            target.setRotation(rotation);
        }

        if (safeOnly) {
            final Optional<ServerLocation> optionalSafeLocation = Sponge.server().teleportHelper().findSafeLocation(location);
            return optionalSafeLocation.isPresent() && target.setLocation(optionalSafeLocation.get());
        }

        if (target.setLocation(location)) {
            return true;
        }

        return target.transferToWorld(location.world(), location.position());
    }

    @Override
    public boolean teleport(final Entity target, final ServerLocation location, final boolean safeOnly) {
        return this.teleport(target, location, null, safeOnly);
    }

}
