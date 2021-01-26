package cosmos.registries.data.serializable;

import org.spongepowered.api.data.persistence.DataSerializable;

import java.util.Optional;

public interface CollectorSerializable<T> extends DataSerializable {

    Optional<T> collect();

}
