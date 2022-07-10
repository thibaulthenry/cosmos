package cosmos.services.io.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.Directories;
import cosmos.services.io.ConfigurationService;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.file.CopyFileVisitor;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Singleton
public class ConfigurationServiceImpl implements ConfigurationService {

    private final FinderService finderService;
    private final HoconConfigurationLoader loader;
    private final ConfigurationNode rootNode;

    @Inject
    public ConfigurationServiceImpl(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.loader = this.buildLoader();
        this.rootNode = this.load();
    }

    private HoconConfigurationLoader buildLoader() {
        try {
            final Optional<Path> optionalConfigPath = this.finderService.findConfigPath(Directories.Files.COSMOS_CONFIG);

            if (!optionalConfigPath.isPresent()) {
                Cosmos.logger().error("Unable to find configuration file");
                return null;
            }

            final Path configPath = optionalConfigPath.get();
            final InputStream configResource = Thread.currentThread().getContextClassLoader().getResourceAsStream(Directories.Files.DEFAULT_CONFIG);

            if (configResource == null) {
                Cosmos.logger().error("Default configuration loading failed");
                return null;
            }

            final File configFile = configPath.toFile();

            if (!configFile.exists()) {
                if (!configFile.getParentFile().mkdirs() && !configFile.createNewFile()) {
                    Cosmos.logger().error("Unable to find or generate configuration file");
                    return null;
                }

                Files.copy(configResource, configPath);
            }

            return HoconConfigurationLoader.builder().path(configPath).build();
        } catch (final Exception e) {
            Cosmos.logger().error("An error occurred while loading configuration file", e);
            return null;
        }
    }

    @Override
    public Optional<ConfigurationNode> findNode(final Object... paths) {
        if (!this.isLoaded()) {
            return Optional.empty();
        }

        return Optional.of(this.rootNode.node(paths));
    }

    @Override
    public boolean isEnabled(final ConfigurationNode configurationNode) {
        return configurationNode.getBoolean();
    }

    @Override
    public boolean isLoaded() {
        return this.rootNode != null;
    }

    @Override
    public ConfigurationNode load() {
        if (this.loader == null) {
            return null;
        }

        try {
            return this.loader.load();
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while loading configuration file", e);
            return null;
        }
    }

    @Override
    public boolean save() {
        if (this.loader == null) {
            return false;
        }

        if (!this.loader.canSave() || !this.rootNode.isMap()) {
            return false;
        }

        try {
            this.loader.save(this.rootNode);

            return true;
        } catch (final Exception e) {
            Cosmos.logger().error("An error occurred while saving configuration file", e);
            return false;
        }
    }

    @Override
    public boolean saveValue(final Object value, final Object... paths) {
        return this.setValue(value, paths) && this.save();
    }

    private boolean setValue(final Object value, final ConfigurationNode node) {
        try {
            node.set(value);

            return true;
        } catch (final Exception e) {
            Cosmos.logger().error("An error occurred while setting value to configuration file", e);
            return false;
        }
    }

    private boolean setValue(final Object value, final Object... paths) {
        return this.findNode(paths).map(node -> this.setValue(value, node)).orElse(false);
    }

}
