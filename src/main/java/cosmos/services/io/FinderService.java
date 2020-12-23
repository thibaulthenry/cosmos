package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.FinderServiceImpl;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataView;

import java.nio.file.Path;
import java.util.Optional;

@ImplementedBy(FinderServiceImpl.class)
public interface FinderService extends CosmosService {

    void writeToFile(DataView dataContainer, Path path);

    Optional<DataContainer> readFromFile(Path path);
}
