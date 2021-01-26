package cosmos;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.root.New;
import cosmos.executors.modules.Root;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.Test;
import cosmos.services.ServiceProvider;
import io.leangen.geantyref.TypeToken;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.CollideBlockEvent;
import org.spongepowered.api.event.cause.entity.MovementType;
import org.spongepowered.api.event.cause.entity.MovementTypes;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

import java.util.List;
import java.util.Optional;

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

        Command.Parameterized cmd = Command.builder()
                .parameter( Parameter.builder(new TypeToken<List<Entity>>() {})
                        .setKey(CosmosKeys.ENTITY_TARGETS)
                        .parser(new Test())
                        .build())
                .setExecutor(injector.getInstance(New.class))
                .build();

        event.register(Cosmos.pluginContainer, cmd, "test");
    }

    @Listener
    public void onRegisterDataEvent(final RegisterDataEvent event) {
        Cosmos.serviceProvider.data().dataBuilder().registerAll();
        //Cosmos.serviceProvider.data().portal().registerAll();
        Cosmos.serviceProvider.data().selector().registerAll();
    }

    @Listener
    public void onStartingServerEvent(final StartingEngineEvent<Server> event) {
        Cosmos.serviceProvider.listener().initializeAll();

        if (!Cosmos.serviceProvider.finder().initDirectories()) {
            Cosmos.logger.warn("An unexpected error occurred while initializing Cosmos directories");
        }
    }

}