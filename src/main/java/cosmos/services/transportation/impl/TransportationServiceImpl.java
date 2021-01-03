package cosmos.services.transportation.impl;

import com.google.inject.Singleton;
import cosmos.services.transportation.TransportationService;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import java.util.Optional;

@Singleton
public class TransportationServiceImpl implements TransportationService {

    @Override
    public boolean isSelf(final Audience src, final Identifiable target) {
        if (!(src instanceof Identifiable)) {
            return false;
        }

        if (target != null) {
            return ((Identifiable) src).getUniqueId().equals(target.getUniqueId());
        }

        return false;
    }

    @Override
    public boolean isSelf(final Identifiable src, final Identifiable target) {
        return target != null && src.getUniqueId().equals(target.getUniqueId());
    }

    @Override
    public boolean mustNotify(final Audience src, final Identifiable target) {
        if (!(src instanceof Identifiable) || !(target instanceof Audience)) {
            return false;
        }

        return !this.isSelf(src, target);
    }

    @Override
    public boolean teleport(final Entity target, final ServerLocation location, @Nullable final Vector3d rotation, final boolean safeOnly) {
        if (rotation != null) {
            target.setRotation(rotation);
        }

        if (safeOnly) {
            final Optional<ServerLocation> optionalSafeLocation = Sponge.getServer().getTeleportHelper().getSafeLocation(location);
            return optionalSafeLocation.isPresent() && target.setLocation(optionalSafeLocation.get());
        }

        if (target.setLocation(location)) {
            return true;
        }

        return target.transferToWorld(location.getWorld(), location.getPosition());
    }

    @Override
    public boolean teleport(final Entity target, final ServerLocation location, final boolean safeOnly) {
        return this.teleport(target, location, null, safeOnly);
    }
}
