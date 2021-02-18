package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.FinderServiceImpl;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@ImplementedBy(FinderServiceImpl.class)
public interface FinderService extends CosmosService {

    void deletePerWorldFiles(ResourceKey worldKey) throws IOException;

    Optional<Path> findConfigPath(String... subs);

    Optional<Path> findCosmosPath(String... subs);

    Optional<Path> findCosmosPath(String directory, ResourceKey worldKey);

    Optional<Path> findCosmosPath(String directory, ServerPlayer player);

    Optional<Path> findCosmosPath(String directory, ServerWorld world);

    Optional<Path> findCosmosPath(String directory, ResourceKey worldKey, ServerPlayer player);

    Optional<Path> findCosmosPath(String directory, ServerWorld world, ServerPlayer player);

    boolean initDirectories();

    Optional<DataContainer> readFromFile(Path path);

    Stream<Path> stream(String directory);

    Optional<Path> worldPath(ResourceKey worldKey);

    void writeToFile(DataSerializable dataSerializable, Path path);
}
