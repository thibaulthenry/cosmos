package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.ConfigurationServiceImpl;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Optional;

@ImplementedBy(ConfigurationServiceImpl.class)
public interface ConfigurationService extends CosmosService {

    Optional<ConfigurationNode> findNode(Object... paths);

    boolean isEnabled(ConfigurationNode configurationNode);

    boolean isLoaded();

    ConfigurationNode load();

    boolean save();

    boolean saveValue(Object value, Object... paths);

}
