package cosmos.services.transportation.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.services.io.FinderService;
import cosmos.services.transportation.TransportationService;
import cosmos.services.validation.ValidationService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import java.text.MessageFormat;
import java.util.Optional;

@Singleton
public class TransportationServiceImpl implements TransportationService {

    private final ValidationService validationService;

    @Inject
    public TransportationServiceImpl(final Injector injector) {
        this.validationService = injector.getInstance(ValidationService.class);
    }

    @Override
    public String buildCommand(@Nullable final String target, @Nullable final ResourceKey worldKey, @Nullable final Vector3d position, @Nullable final Vector3d rotation, final boolean safeOnly) {
        return MessageFormat.format(
                "/cm move{0}{1}{2}{3}{4}",
                safeOnly ? " --safe-only" : "",
                target == null ? "" : " " + target,
                worldKey == null ? "" : " " + worldKey.getFormatted(),
                position == null ? "" : " " + position.getX() + " " + position.getY() + " " + position.getZ(),
                rotation == null ? "" : " " + rotation.getX() + " " + rotation.getY() + " " + rotation.getZ()
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
