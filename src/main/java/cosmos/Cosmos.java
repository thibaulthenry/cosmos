package cosmos;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.modules.Root;
import cosmos.services.ServiceProvider;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

@Plugin(value = "cosmos")
public class Cosmos {

    public static final String NAMESPACE = "cosmos";

    private static Logger logger;
    private static PluginContainer pluginContainer;
    private static ServiceProvider serviceProvider;
    private final Injector injector;

    @Inject
    public Cosmos(final Injector injector, final PluginContainer pluginContainer, final Logger logger) {
        Cosmos.logger = logger;
        Cosmos.pluginContainer = pluginContainer;
        Cosmos.serviceProvider = injector.getInstance(ServiceProvider.class);
        this.injector = injector;
    }

    public static Logger getLogger() {
        return Cosmos.logger;
    }

    public static PluginContainer getPluginContainer() {
        return Cosmos.pluginContainer;
    }

    public static ServiceProvider getServices() {
        return Cosmos.serviceProvider;
    }

    @Listener
    public void onRegisterCommandEvent(final RegisterCommandEvent<Command.Parameterized> event) {
        event.register(Cosmos.pluginContainer, this.injector.getInstance(Root.class).getParametrized(), "cosmos", "cm");
    }

    @Listener
    public void onRegisterDataEvent(final RegisterDataEvent event) {
        Cosmos.serviceProvider.dataBuilder().registerAll();
    }

    @Listener
    public void onStartingServerEvent(final StartingEngineEvent<Server> event) {
        // todo bug inventory Cosmos.serviceProvider.listener().initializeAll();

        if (!Cosmos.serviceProvider.finder().initDirectories()) {
            Cosmos.logger.warn("An unexpected error occurred while initializing Cosmos directories");
        }
    }
}