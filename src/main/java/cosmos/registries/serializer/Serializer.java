package cosmos.registries.serializer;

import java.nio.file.Path;
import java.util.Optional;

public interface Serializer<T> {

    Optional<T> deserialize(Path path);

    void serialize(Path path, T data);

}
