package cosmos.registries.listener;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.ConfigurationNodes;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.listener.impl.perworld.AdvancementListener;
import cosmos.registries.listener.impl.perworld.ChatListener;
import cosmos.registries.listener.impl.perworld.CommandBlockListener;
import cosmos.registries.listener.impl.perworld.ExperienceListener;
import cosmos.registries.listener.impl.perworld.GameModeListener;
import cosmos.registries.listener.impl.perworld.HealthListener;
import cosmos.registries.listener.impl.perworld.HungerListener;
import cosmos.registries.listener.impl.perworld.InventoryListener;
import cosmos.registries.listener.impl.perworld.ScoreboardListener;
import cosmos.registries.listener.impl.perworld.TabListListener;
import cosmos.registries.listener.impl.portal.PortalDispatcherListener;
import cosmos.registries.listener.impl.portal.PortalGenerationListener;
import cosmos.registries.listener.impl.portal.PortalProtectionListener;
import cosmos.registries.listener.impl.portal.PortalTeleportListener;
import cosmos.services.io.ConfigurationService;
import cosmos.services.listener.ListenerService;
import org.spongepowered.api.Sponge;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton
public class ListenerRegistry implements CosmosRegistry<Class<? extends Listener>, Listener> {

    private final Map<Class<? extends Listener>, Listener> listenerMap = new HashMap<>();

    private final ConfigurationService configurationService;
    private final ListenerService listenerService;

    @Inject
    public ListenerRegistry(final Injector injector) {
        this.configurationService = injector.getInstance(ConfigurationService.class);
        this.listenerService = injector.getInstance(ListenerService.class);

        this.listenerMap.put(AdvancementListener.class, injector.getInstance(AdvancementListener.class));
        this.listenerMap.put(ChatListener.class, injector.getInstance(ChatListener.class));
        this.listenerMap.put(CommandBlockListener.class, injector.getInstance(CommandBlockListener.class));
        this.listenerMap.put(ExperienceListener.class, injector.getInstance(ExperienceListener.class));
        this.listenerMap.put(GameModeListener.class, injector.getInstance(GameModeListener.class));
        this.listenerMap.put(HealthListener.class, injector.getInstance(HealthListener.class));
        this.listenerMap.put(HungerListener.class, injector.getInstance(HungerListener.class));
        this.listenerMap.put(InventoryListener.class, injector.getInstance(InventoryListener.class));
        this.listenerMap.put(PortalDispatcherListener.class, injector.getInstance(PortalDispatcherListener.class));
        this.listenerMap.put(PortalGenerationListener.class, injector.getInstance(PortalGenerationListener.class));
        this.listenerMap.put(PortalProtectionListener.class, injector.getInstance(PortalProtectionListener.class));
        this.listenerMap.put(PortalTeleportListener.class, injector.getInstance(PortalTeleportListener.class));
        this.listenerMap.put(ScoreboardListener.class, injector.getInstance(ScoreboardListener.class));
        this.listenerMap.put(TabListListener.class, injector.getInstance(TabListListener.class));
    }

    public Optional<CosmosRegistryEntry<Class<? extends Listener>, Listener>> register(final Class<? extends Listener> key) {
        return this.find(key).flatMap(v -> this.register(key, v));
    }

    @Override
    public Optional<CosmosRegistryEntry<Class<? extends Listener>, Listener>> register(final Class<? extends Listener> key, final Listener value) {
        return Optional.ofNullable(this.listenerMap.computeIfAbsent(key, k -> value))
                .map(v -> {
                    if (v.registeredToSponge()) {
                        return CosmosRegistryEntry.of(key, v);
                    }

                    if (v instanceof ToggleListener) {
                        ((ToggleListener) v).start();
                    }

                    v.registeredToSponge(true);
                    Sponge.eventManager().registerListeners(Cosmos.pluginContainer(), v);

                    if (v instanceof ScheduledSaveListener) {
                        this.listenerService.submitSaveTaskIfNot();
                    }

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public void registerAll() {
        if (!this.configurationService.isLoaded()) {
            Cosmos.logger().error("Configuration file not loaded. All listeners disabled !");
            return;
        }

        this.streamEntries()
                .filter(entry -> {
                    if (!entry.value().configurable()) {
                        return true;
                    }

                    final String listenerNode = this.listenerService.format(entry.key());

                    return this.configurationService.findNode(ConfigurationNodes.PER_WORLD, listenerNode, ConfigurationNodes.PER_WORLD_STATE)
                            .map(this.configurationService::isEnabled)
                            .orElse(false);
                })
                .forEach(entry -> this.register(entry.key(), entry.value()));
    }

    @Override
    public Stream<Listener> stream() {
        return this.listenerMap.values().stream();
    }

    @Override
    public Stream<CosmosRegistryEntry<Class<? extends Listener>, Listener>> streamEntries() {
        return this.listenerMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public Optional<CosmosRegistryEntry<Class<? extends Listener>, Listener>> unregister(final Class<? extends Listener> key) {
        return this.find(key)
                .map(v -> {
                    if (!v.registeredToSponge()) {
                        return CosmosRegistryEntry.of(key, v);
                    }

                    v.registeredToSponge(false);
                    Sponge.eventManager().unregisterListeners(v);

                    if (v instanceof ToggleListener) {
                        ((ToggleListener) v).stop();
                    }

                    final boolean hasScheduledSaveListenerRegistered = this.stream()
                            .filter(l -> l instanceof ScheduledSaveListener)
                            .anyMatch(Listener::registeredToSponge);

                    if (!hasScheduledSaveListenerRegistered) {
                        this.listenerService.cancelSaveTaskIfNot();
                    }

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    public void unregisterAll() {
        this.streamEntries().forEach(entry -> Sponge.eventManager().unregisterListeners(entry.value()));
    }

    @Override
    public Listener value(final Class<? extends Listener> key) {
        return this.listenerMap.get(key);
    }

}
