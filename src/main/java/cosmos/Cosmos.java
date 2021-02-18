package cosmos;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.border.Center;
import cosmos.executors.commands.border.Size;
import cosmos.executors.commands.root.Move;
import cosmos.executors.commands.root.New;
import cosmos.executors.modules.Root;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.Test;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.impl.CosmosFramePortalBuilder;
import cosmos.services.ServiceProvider;
import io.leangen.geantyref.TypeToken;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterBuilderEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void onRegisterBuilderEvent(final RegisterBuilderEvent event) {
        event.register(CosmosPortal.Builder.class, () -> this.injector.getInstance(CosmosFramePortalBuilder.class));
    }

    @Listener
    public void onRegisterCommandEvent(final RegisterCommandEvent<Command.Parameterized> event) {
        event.register(Cosmos.pluginContainer, this.injector.getInstance(Root.class).getParametrized(), "cosmos", "cm");
    }

    @Listener
    public void onRegisterDataEvent(final RegisterDataEvent event) {
        Cosmos.serviceProvider.data().registerBuilders();
        Cosmos.serviceProvider.data().registerPortalTypes();
        Cosmos.serviceProvider.data().registerSelectors();
    }

    @Listener
    public void onStartingServerEvent(final StartingEngineEvent<Server> event) {
        Cosmos.serviceProvider.listener().initializeAll();

        if (!Cosmos.serviceProvider.finder().initDirectories()) {
            Cosmos.logger.error("An unexpected error occurred while initializing Cosmos directories");
        }

        Task task = Task.builder()
                .plugin(Cosmos.pluginContainer)
                .interval(Ticks.of(20))
                .execute(() -> {
                    serviceProvider.registry().portal()
                            .stream()
                            .filter(portal -> portal.getParticles() != null)
                            .forEach(portal -> {
                                portal.getOrigins().forEach(location -> {
                                    location.getWorld().spawnParticles(portal.getParticles(), location.getPosition().add(0, 0.5, 0));
                                });
                            });
                })
                .name("test")
                .build();

        Sponge.getAsyncScheduler().submit(task);
    }

}