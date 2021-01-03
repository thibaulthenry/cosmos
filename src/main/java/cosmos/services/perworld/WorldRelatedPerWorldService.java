package cosmos.services.perworld;

import cosmos.services.CosmosService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.world.server.ServerWorld;

import java.nio.file.Path;
import java.util.Optional;

public interface WorldRelatedPerWorldService extends CosmosService {

    Optional<Path> getPath(ResourceKey worldKey);

    Optional<Path> getPath(ServerWorld world);

}
