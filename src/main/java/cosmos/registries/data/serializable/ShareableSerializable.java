package cosmos.registries.data.serializable;

import org.spongepowered.api.data.persistence.DataSerializable;

public interface ShareableSerializable<T> extends DataSerializable {

    void share(T data);

}
