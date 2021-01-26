package cosmos.services.io;

import com.google.inject.ImplementedBy;
import cosmos.registries.listener.Listener;
import cosmos.services.CosmosService;
import cosmos.services.io.impl.ConfigurationServiceImpl;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Optional;

@ImplementedBy(ConfigurationServiceImpl.class)
public interface ConfigurationService extends CosmosService {

    String formatListener(Class<? extends Listener> listener);

    Optional<ConfigurationNode> getNode(Object... paths);

    boolean isEnabled(ConfigurationNode configurationNode);

    boolean isLoaded();

    ConfigurationNode load();

    boolean save();

    boolean save(ConfigurationNode node);

    boolean saveValue(Object value, Object startSaveAt, Object... paths);

    boolean saveValue(Object value, ConfigurationNode savedNode, Object... paths);

    boolean saveValue(Object value, ConfigurationNode savedNode, ConfigurationNode valueNode);

}
