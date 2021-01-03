package cosmos.services.perworld;

import cosmos.services.CosmosService;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;

import java.nio.file.Path;
import java.util.Optional;

public interface PlayerRelatedPerWorldService extends CosmosService {

    Optional<Path> getPath(ServerWorld world, ServerPlayer player);

    Optional<Path> getPath(ServerPlayer player);

}
