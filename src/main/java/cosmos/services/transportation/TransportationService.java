package cosmos.services.transportation;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.transportation.impl.TransportationServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

@ImplementedBy(TransportationServiceImpl.class)
public interface TransportationService extends CosmosService {

    String buildCommand(@Nullable String target, @Nullable ResourceKey worldKey, @Nullable Vector3d position, @Nullable Vector3d rotation, boolean safeOnly);

    boolean isSelf(Audience src, Identifiable target);

    boolean isSelf(Identifiable src, Identifiable target);

    boolean mustNotify(Audience src, Identifiable target);

    boolean teleport(Entity target, ServerLocation location, @Nullable Vector3d rotation, boolean safeOnly);

    boolean teleport(Entity target, ServerLocation location, boolean safeOnly);
}
