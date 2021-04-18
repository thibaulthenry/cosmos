package cosmos.statics.config;

import cosmos.Cosmos;
import cosmos.listeners.AbstractListener;
import cosmos.statics.finders.FinderFile;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("StaticVariableUsedBeforeInitialization")
public class Config {

    private static final String DEFAULT_CONF_FILE_NAME = "default.conf";
    private static final String COSMOS_CONF_FILE_NAME = "cosmos.conf";

    private static final String TIME_KEY = "time";
    private static final String REAL_TIME_WORLDS_KEY = "realtimeworlds";
    private static final String REAL_TIME_SMOOTH_UPDATE_KEY = "realtimesmoothupdate";
    private static final String IGNORE_PLAYERS_SLEEPING_WORLDS_KEY = "ignoreplayerssleepingworlds";

    private static ConfigurationNode rootNode;

    private static ConfigurationLoader<CommentedConfigurationNode> configurationLoader;

    private static Optional<ConfigurationNode> getRootNode() {
        return Optional.ofNullable(rootNode);
    }

    private static Optional<ConfigurationNode> getListenerNode(Class<? extends AbstractListener> listenerClass) {
        return getRootNode()
                .map(configurationNode -> configurationNode
                        .getNode(StringUtils.removeStart(listenerClass.getPackage().getName(), "cosmos.listeners.").toLowerCase())
                        .getNode(StringUtils.removeEnd(listenerClass.getSimpleName(), "Listener").toLowerCase())
                );
    }

    public static Optional<ConfigurationNode> getGroupNode(Class<? extends AbstractListener> listenerClass) {
        return getRootNode()
                .map(configurationNode -> configurationNode
                        .getNode(StringUtils.removeStart(listenerClass.getPackage().getName(), "cosmos.listeners.").toLowerCase())
                        .getNode(StringUtils.removeEnd(listenerClass.getSimpleName(), "Listener").toLowerCase() + "-groups")
                );
    }

    private static Optional<ConfigurationNode> getPersistOnActivationNode(Class<? extends AbstractListener> listenerClass) {
        return getRootNode()
                .map(configurationNode -> configurationNode
                        .getNode(StringUtils.removeStart(listenerClass.getPackage().getName(), "cosmos.listeners.").toLowerCase())
                        .getNode(StringUtils.removeEnd(listenerClass.getSimpleName(), "Listener").toLowerCase() + "-persist-on-activation")
                );
    }

    private static Optional<ConfigurationNode> getTimeNode(String key) {
        return getRootNode()
                .map(configurationNode -> configurationNode
                        .getNode(TIME_KEY)
                        .getNode(key)
                );
    }

    private static boolean isWorldInNodeList(ConfigurationNode configurationNode, WorldProperties worldProperties) {
        return configurationNode.getChildrenList()
                .stream()
                .anyMatch(worldUUIDNode -> worldProperties.getUniqueId().toString().equals(worldUUIDNode.getString()));
    }

    private static boolean saveWorldInNodeList(ConfigurationNode configurationNode, WorldProperties worldProperties, boolean state) {
        String uuid = worldProperties.getUniqueId().toString();

        List<? extends ConfigurationNode> uuidNodes = configurationNode.getChildrenList();

        if (state) {
            if (uuidNodes.stream().anyMatch(uuidNode -> uuid.equals(uuidNode.getString()))) {
                return true;
            }

            configurationNode.appendListNode().setValue(uuid);
        } else {
            uuidNodes
                    .stream()
                    .filter(uuidNode -> uuid.equals(uuidNode.getString()))
                    .map(ConfigurationNode::getKey)
                    .filter(Objects::nonNull)
                    .forEach(configurationNode::removeChild);
        }

        return save();
    }

    public static boolean saveListener(Class<? extends AbstractListener> listenerClass, boolean state) {
        return getListenerNode(listenerClass)
                .map(listenerNode -> {
                    listenerNode.setValue(state);
                    return save();
                })
                .orElse(false);
    }

    public static boolean saveGroup(Class<? extends AbstractListener> listenerClass, Collection<Set<String>> group) {
        return getGroupNode(listenerClass)
                .map(listenerNode -> {
                    listenerNode.setValue(group);
                    return save();
                })
                .orElse(false);
    }

    public static boolean isListenerEnabled(Class<? extends AbstractListener> listenerClass) {
        return getListenerNode(listenerClass)
                .map(ConfigurationNode::getBoolean)
                .orElse(false);
    }

    public static boolean isPersistedOnActivation(Class<? extends AbstractListener> listenerClass) {
        return getPersistOnActivationNode(listenerClass)
                .map(ConfigurationNode::getBoolean)
                .orElse(false);
    }

    public static boolean saveRealTime(WorldProperties worldProperties, boolean state) {
        return getTimeNode(REAL_TIME_WORLDS_KEY)
                .map(worldsNode -> saveWorldInNodeList(worldsNode, worldProperties, state))
                .orElse(false);
    }

    public static boolean isRealTimeWorldEnabled(WorldProperties worldProperties) {
        return getTimeNode(REAL_TIME_WORLDS_KEY)
                .map(configurationNode -> isWorldInNodeList(configurationNode, worldProperties))
                .orElse(false);
    }

    public static boolean isRealTimeSmooth() {
        return getTimeNode(REAL_TIME_SMOOTH_UPDATE_KEY)
                .map(ConfigurationNode::getBoolean)
                .orElse(false);
    }

    public static boolean saveIgnorePlayerSleeping(WorldProperties worldProperties, boolean state) {
        return getTimeNode(IGNORE_PLAYERS_SLEEPING_WORLDS_KEY)
                .map(worldsNode -> saveWorldInNodeList(worldsNode, worldProperties, state))
                .orElse(false);
    }

    public static boolean isIgnorePlayersSleepingWorldEnabled(WorldProperties worldProperties) {
        return getTimeNode(IGNORE_PLAYERS_SLEEPING_WORLDS_KEY)
                .map(configurationNode -> isWorldInNodeList(configurationNode, worldProperties))
                .orElse(false);
    }

    public static void load() {
        Optional<Path> optionalConfigPath = FinderFile.getConfigPath(COSMOS_CONF_FILE_NAME);

        if (!optionalConfigPath.isPresent()) {
            Cosmos.sendConsole(Text.of(TextColors.RED, "Unable to find Cosmos configuration file"));
            return;
        }

        Path configPath = optionalConfigPath.get();

        Optional<Asset> optionalDefaultConfigAsset = Sponge.getAssetManager()
                .getAsset(Cosmos.instance, DEFAULT_CONF_FILE_NAME);

        if (!optionalDefaultConfigAsset.isPresent()) {
            Cosmos.sendConsole(Text.of(TextColors.RED, "Default configuration loading failed"));
            return;
        }

        Text loadingErrorText = Text.of(TextColors.RED, "An error occurred while loading configuration file");

        try {
            File configFile = configPath.toFile();

            if (!configFile.exists() && !configFile.getParentFile().mkdirs() && !configFile.createNewFile()) {
                Cosmos.sendConsole(loadingErrorText);
                return;
            }

            optionalDefaultConfigAsset.get().copyToFile(configPath, false, true);

            configurationLoader = HoconConfigurationLoader
                    .builder()
                    .setPath(configPath)
                    .build();

            if (!configurationLoader.canLoad()) {
                Cosmos.sendConsole(loadingErrorText);
            }

            rootNode = configurationLoader.load();
        } catch (IOException ignored) {
            Cosmos.sendConsole(loadingErrorText);
        }
    }

    public static boolean save() {
        if (configurationLoader == null || !configurationLoader.canSave()) {
            return false;
        }

        try {
            configurationLoader.save(rootNode);

            return true;
        } catch (IOException ignored) {
            Cosmos.sendConsole(Text.of(TextColors.RED, "An error occurred while saving configuration file"));
            return false;
        }
    }

    public static boolean isLoaded() {
        return getRootNode().isPresent();
    }
}
