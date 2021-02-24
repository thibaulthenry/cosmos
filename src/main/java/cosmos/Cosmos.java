package cosmos;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.modules.Root;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.impl.CosmosFramePortalBuilderImpl;
import cosmos.services.ServiceProvider;
import cosmos.services.registry.RegistryProvider;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterBuilderEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

import java.util.Random;

@Plugin(value = "cosmos")
public class Cosmos {

    public static final String NAMESPACE = "cosmos";

    private static Logger logger;
    private static PluginContainer pluginContainer;
    private static RegistryProvider registryProvider;
    private static ServiceProvider serviceProvider;
    private final Injector injector;

    @Inject
    public Cosmos(final Injector injector, final PluginContainer pluginContainer, final Logger logger) {
        Cosmos.logger = logger;
        Cosmos.pluginContainer = pluginContainer;
        Cosmos.registryProvider = injector.getInstance(RegistryProvider.class);
        Cosmos.serviceProvider = injector.getInstance(ServiceProvider.class);
        this.injector = injector;
    }

    public static Logger getLogger() {
        return Cosmos.logger;
    }

    public static PluginContainer getPluginContainer() {
        return Cosmos.pluginContainer;
    }

    public static RegistryProvider getRegistries() {
        return Cosmos.registryProvider;
    }

    public static ServiceProvider getServices() {
        return Cosmos.serviceProvider;
    }

    @Listener
    public void onRegisterBuilderEvent(final RegisterBuilderEvent event) {
        event.register(CosmosFramePortal.Builder.class, () -> this.injector.getInstance(CosmosFramePortalBuilderImpl.class));
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

        Random var4 = new Random();

        Task task = Task.builder()
                .plugin(Cosmos.pluginContainer)
                .interval(Ticks.of(20))
                .execute(() -> {
                    serviceProvider.registry().portal()
                            .stream()
                            .forEach(portal -> {
                                portal.origins().forEach(location -> {

                                    for(int var5 = 0; var5 < 3; ++var5) {
                                        int var6 = var4.nextInt(2) * 2 - 1;
                                        int var7 = var4.nextInt(2) * 2 - 1;
                                        double var8 = 0.25D * (double)var6;
                                        double var10 = var4.nextFloat();
                                        double var12 = 0.25D * (double)var7;
                                        double var14 = (double)(var4.nextFloat() * (float)var6);
                                        double var16 = ((double)var4.nextFloat() - 0.5D) * 0.125D;
                                        double var18 = (double)(var4.nextFloat() * (float)var7);

                                        ParticleEffect particleEffect = ParticleEffect.builder()
                                                .type(portal.particles().map(ParticleEffect::getType).orElse(ParticleTypes.PORTAL.get()))
                                                .quantity(3)
                                                .offset(Vector3d.from(var8, var10, var12))
                                                .velocity(Vector3d.from(var14, var16, var18))
                                                .build();

                                        location.getWorld().spawnParticles(particleEffect, location.getPosition().add(0.5, 0.5, 0.5));

                                        portal.soundAmbiance().ifPresent(sound -> {
                                            if (var4.nextInt(25*portal.originsSize()) == 0) {
                                                location.getWorld().playSound(sound);
                                            }
                                        });

                                    }
                                });
                            });
                })
                .name("test")
                .build();

        Sponge.getAsyncScheduler().submit(task);
    }

}