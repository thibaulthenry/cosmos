package cosmos.registries.listener;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
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
import cosmos.registries.listener.impl.portal.PortalButtonListener;
import cosmos.registries.listener.impl.portal.PortalDispatcherListener;
import cosmos.registries.listener.impl.portal.PortalFrameListener;
import cosmos.registries.listener.impl.portal.PortalGenerationListener;
import cosmos.registries.listener.impl.portal.PortalProtectionListener;
import cosmos.registries.listener.impl.portal.PortalSignListener;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class ListenerRegistry implements CosmosRegistry<Class<? extends Listener>, Listener> {

    private final Map<Class<? extends Listener>, Listener> listenerMap = new HashMap<>();

    @Inject
    public ListenerRegistry(final Injector injector) {
        this.listenerMap.put(AdvancementsListener.class, injector.getInstance(AdvancementsListener.class));
        this.listenerMap.put(ChatsListener.class, injector.getInstance(ChatsListener.class));
        this.listenerMap.put(CommandBlocksListener.class, injector.getInstance(CommandBlocksListener.class));
        this.listenerMap.put(ExperiencesListener.class, injector.getInstance(ExperiencesListener.class));
        this.listenerMap.put(GameModesListener.class, injector.getInstance(GameModesListener.class));
        this.listenerMap.put(HealthsListener.class, injector.getInstance(HealthsListener.class));
        this.listenerMap.put(HungersListener.class, injector.getInstance(HungersListener.class));
        this.listenerMap.put(InventoriesListener.class, injector.getInstance(InventoriesListener.class));
        this.listenerMap.put(PortalButtonListener.class, injector.getInstance(PortalButtonListener.class));
        this.listenerMap.put(PortalDispatcherListener.class, injector.getInstance(PortalDispatcherListener.class));
        this.listenerMap.put(PortalFrameListener.class, injector.getInstance(PortalFrameListener.class));
        this.listenerMap.put(PortalGenerationListener.class, injector.getInstance(PortalGenerationListener.class));
        this.listenerMap.put(PortalProtectionListener.class, injector.getInstance(PortalProtectionListener.class));
        this.listenerMap.put(PortalSignListener.class, injector.getInstance(PortalSignListener.class));
        this.listenerMap.put(ScoreboardsListener.class, injector.getInstance(ScoreboardsListener.class));
        this.listenerMap.put(TabListsListener.class, injector.getInstance(TabListsListener.class));
    }

    public Stream<Listener> stream() {
        return this.listenerMap.values().stream();
    }

    public Stream<CosmosRegistryEntry<Class<? extends Listener>, Listener>> streamEntries() {
        return this.listenerMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public Listener value(final Class<? extends Listener> key) {
        return this.listenerMap.get(key);
    }

}
