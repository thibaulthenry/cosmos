package cosmos.registries.listener;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.ConfigurationNodes;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.listener.impl.perworld.AdvancementsListener;
import cosmos.registries.listener.impl.perworld.ChatsListener;
import cosmos.registries.listener.impl.perworld.CommandBlocksListener;
import cosmos.registries.listener.impl.perworld.ExperiencesListener;
import cosmos.registries.listener.impl.perworld.GameModesListener;
import cosmos.registries.listener.impl.perworld.HealthsListener;
import cosmos.registries.listener.impl.perworld.HungersListener;
import cosmos.registries.listener.impl.perworld.InventoriesListener;
import cosmos.registries.listener.impl.perworld.ScoreboardsListener;
import cosmos.registries.listener.impl.perworld.TabListsListener;
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

        this.listenerMap.put(AdvancementsListener.class, injector.getInstance(AdvancementsListener.class));
        this.listenerMap.put(ChatsListener.class, injector.getInstance(ChatsListener.class));
        this.listenerMap.put(CommandBlocksListener.class, injector.getInstance(CommandBlocksListener.class));
        this.listenerMap.put(ExperiencesListener.class, injector.getInstance(ExperiencesListener.class));
        this.listenerMap.put(GameModesListener.class, injector.getInstance(GameModesListener.class));
        this.listenerMap.put(HealthsListener.class, injector.getInstance(HealthsListener.class));
        this.listenerMap.put(HungersListener.class, injector.getInstance(HungersListener.class));
        this.listenerMap.put(InventoriesListener.class, injector.getInstance(InventoriesListener.class));
        this.listenerMap.put(PortalDispatcherListener.class, injector.getInstance(PortalDispatcherListener.class));
        this.listenerMap.put(PortalGenerationListener.class, injector.getInstance(PortalGenerationListener.class));
        this.listenerMap.put(PortalProtectionListener.class, injector.getInstance(PortalProtectionListener.class));
        this.listenerMap.put(PortalTeleportListener.class, injector.getInstance(PortalTeleportListener.class));
        this.listenerMap.put(ScoreboardsListener.class, injector.getInstance(ScoreboardsListener.class));
        this.listenerMap.put(TabListsListener.class, injector.getInstance(TabListsListener.class));
    }

    public boolean isRegisteredToSponge(final Class<? extends Listener> clazz) {
        return this.has(clazz) && this.value(clazz).registeredToSponge();
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

                    return this.configurationService.findNode(ConfigurationNodes.PER_WORLD, listenerNode)
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
        return Optional.ofNullable(this.listenerMap.remove(key))
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
