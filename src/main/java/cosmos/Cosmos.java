package cosmos;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.modules.Root;
import cosmos.services.ServiceProvider;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

@Plugin(value = "cosmos")
public class Cosmos {

    private final Injector injector;
    private static PluginContainer pluginContainer;
    private static Logger logger;
    private static ServiceProvider serviceProvider;

    @Inject
    public Cosmos(final Injector injector, final PluginContainer pluginContainer, final Logger logger) {
        this.injector = injector;
        Cosmos.pluginContainer = pluginContainer;
        Cosmos.logger = logger;
        Cosmos.serviceProvider = injector.getInstance(ServiceProvider.class);
    }

    public static PluginContainer getPluginContainer() {
        return Cosmos.pluginContainer;
    }

    public static Logger getLogger() {
        return Cosmos.logger;
    }

    public static ServiceProvider getServices() {
        return Cosmos.serviceProvider;
    }

    @Listener
    public void onRegisterCommandEvent(final RegisterCommandEvent<Command.Parameterized> event) {
        event.register(Cosmos.pluginContainer, this.injector.getInstance(Root.class).getParametrized(), "cm");
        ;
    }
}