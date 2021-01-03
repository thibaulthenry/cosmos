package cosmos.registries.serializer;

import java.nio.file.Path;
import java.util.Optional;

public interface Serializer<T> {

    void serialize(Path path, T data);

    Optional<T> deserialize(Path path);

}
