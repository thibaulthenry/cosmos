package cosmos.services.listener.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.ConfigurationNodes;
import cosmos.registries.listener.Listener;
import cosmos.registries.listener.ListenerRegistry;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.listener.ScheduledSaveListener;
import cosmos.registries.listener.ToggleListener;
import cosmos.services.io.ConfigurationService;
import cosmos.services.listener.ListenerService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class ListenerServiceImpl implements ListenerService {

    private final ConfigurationService configurationService;
    private final ListenerRegistry listenerRegistry;
    private UUID saveTaskUuid;

    @Inject
    public ListenerServiceImpl(final Injector injector) {
        this.configurationService = injector.getInstance(ConfigurationService.class);
        this.listenerRegistry = injector.getInstance(ListenerRegistry.class);
    }

    @Override
    public void cancelSaveTaskIfNot() {
        if (this.saveTaskUuid != null) {
            Sponge.getAsyncScheduler().getTaskById(this.saveTaskUuid).ifPresent(ScheduledTask::cancel);
            this.saveTaskUuid = null;
        }
    }

    @Override
    public void initializeAll() {
        if (!this.configurationService.isLoaded()) {
            Cosmos.getLogger().error("Configuration file not loaded. All listeners disabled !");
            return;
        }

        this.listenerRegistry.entries()
                .stream()
                .filter(entry -> {
                    if (!entry.getValue().isConfigurable()) {
                        return true;
                    }

                    final String listenerNode = this.configurationService.formatListener(entry.getKey());

                    return this.configurationService.getNode(ConfigurationNodes.PER_WORLD, listenerNode)
                            .map(this.configurationService::isEnabled)
                            .orElse(false);
                })
                .forEach(entry -> this.register(entry.getKey()));
    }

    @Override
    public boolean isRegistered(final Class<? extends Listener> clazz) {
        return this.listenerRegistry.has(clazz) && this.listenerRegistry.get(clazz).isRegistered();
    }

    @Override
    public void register(final Class<? extends Listener> clazz) {
        if (this.isRegistered(clazz)) {
            return;
        }

        final Listener listener = this.listenerRegistry.get(clazz);

        if (listener instanceof ToggleListener) {
            ((ToggleListener) listener).start();
        }

        listener.setRegistered(true);
        Sponge.getEventManager().registerListeners(Cosmos.getPluginContainer(), listener);

        this.submitSaveTaskIfNot();
    }

    private void saveTaskExecutor() {
        this.listenerRegistry.values().forEach(listener -> {
            if (!listener.isRegistered()) {
                return;
            }

            if (listener instanceof ScheduledAsyncSaveListener) {
                ((ScheduledAsyncSaveListener) listener).save();
            } else if (listener instanceof ScheduledSaveListener) {
                final Task task = Task.builder()
                        .execute(((ScheduledSaveListener) listener)::save)
                        .plugin(Cosmos.getPluginContainer())
                        .build();

                this.saveTaskUuid = Sponge.getServer().getScheduler().submit(task).getUniqueId();
            }
        });
    }

    private void submitSaveTaskIfNot() {
        if (this.saveTaskUuid != null && Sponge.getAsyncScheduler().getTaskById(this.saveTaskUuid).isPresent()) {
            return;
        }

        final Task task = Task.builder()
                .execute(this::saveTaskExecutor)
                .delay(1, TimeUnit.MINUTES)
                .interval(1, TimeUnit.MINUTES)
                .plugin(Cosmos.getPluginContainer())
                .build();

        this.saveTaskUuid = Sponge.getServer().getScheduler().submit(task).getUniqueId();
    }

    @Override
    public void unregister(final Class<? extends Listener> clazz) {
        if (!this.isRegistered(clazz)) {
            return;
        }

        final Listener listener = this.listenerRegistry.get(clazz);

        listener.setRegistered(false);
        Sponge.getEventManager().unregisterListeners(listener);

        if (listener instanceof ToggleListener) {
            ((ToggleListener) listener).stop();
        }

        final boolean hasScheduledSaveListenerRegistered = this.listenerRegistry.values()
                .stream()
                .filter(l -> l instanceof ScheduledSaveListener)
                .anyMatch(Listener::isRegistered);

        if (!hasScheduledSaveListenerRegistered) {
            this.cancelSaveTaskIfNot();
        }
    }

    @Override
    public void unregisterAll() {
        this.listenerRegistry.entries().forEach(entry -> Sponge.getEventManager().unregisterListeners(entry.getValue()));
    }

}
