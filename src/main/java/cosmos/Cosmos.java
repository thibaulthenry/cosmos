package cosmos;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.root.New;
import cosmos.executors.modules.Root;
import cosmos.executors.parameters.scoreboard.ScoreHolders;
import cosmos.registries.portal.CosmosButtonPortal;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPressurePlatePortal;
import cosmos.registries.portal.CosmosSignPortal;
import cosmos.registries.portal.impl.CosmosButtonPortalBuilderImpl;
import cosmos.registries.portal.impl.CosmosFramePortalBuilderImpl;
import cosmos.registries.portal.impl.CosmosPressurePlatePortalBuilderImpl;
import cosmos.registries.portal.impl.CosmosSignPortalBuilderImpl;
import cosmos.services.ServiceProvider;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterBuilderEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

import java.util.List;

@Plugin(value = "cosmos")
public class Cosmos {

    public static final String NAMESPACE = "cosmos";

    private static Logger logger;
    private static PluginContainer pluginContainer;
    private static ServiceProvider serviceProvider;

    private final Injector injector;

    @Inject
    public Cosmos(final Injector injector) {
        Cosmos.logger = injector.getInstance(Logger.class);
        Cosmos.pluginContainer = injector.getInstance(PluginContainer.class);
        Cosmos.serviceProvider = injector.getInstance(ServiceProvider.class);
        this.injector = injector;
    }

    public static Logger logger() {
        return Cosmos.logger;
    }

    public static PluginContainer pluginContainer() {
        return Cosmos.pluginContainer;
    }

    public static ServiceProvider services() {
        return Cosmos.serviceProvider;
    }

    @Listener
    public void onRegisterBuilderEvent(final RegisterBuilderEvent event) {
        event.register(CosmosButtonPortal.Builder.class, () -> this.injector.getInstance(CosmosButtonPortalBuilderImpl.class));
        event.register(CosmosFramePortal.Builder.class, () -> this.injector.getInstance(CosmosFramePortalBuilderImpl.class));
        event.register(CosmosPressurePlatePortal.Builder.class, () -> this.injector.getInstance(CosmosPressurePlatePortalBuilderImpl.class));
        event.register(CosmosSignPortal.Builder.class, () -> this.injector.getInstance(CosmosSignPortalBuilderImpl.class));
    }

    @Listener
    public void onRegisterCommandEvent(final RegisterCommandEvent<Command.Parameterized> event) {
        event.register(Cosmos.pluginContainer, this.injector.getInstance(Root.class).parametrized(), "cosmos", "cm");
    }

    @Listener
    public void onRegisterDataEvent(final RegisterDataEvent event) {
        Cosmos.serviceProvider.registry().dataBuilder().registerAll();
        Cosmos.serviceProvider.registry().portalType().registerAll();
        Cosmos.serviceProvider.registry().selector().registerAll();
    }

    @Listener
    public void onStartingServerEvent(final StartingEngineEvent<Server> event) {
        Cosmos.serviceProvider.registry().listener().registerAll();

        if (!Cosmos.serviceProvider.finder().initDirectories()) {
            Cosmos.logger.error("An unexpected error occurred while initializing Cosmos directories");
        }
    }

    @Listener
    public void onStartedServerEvent(final StartedEngineEvent<Server> event) {
        Cosmos.serviceProvider.registry().portal().registerAll();
    }

}