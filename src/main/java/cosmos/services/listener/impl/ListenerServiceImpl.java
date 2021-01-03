package cosmos.services.listener.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.*;
import cosmos.services.listener.ListenerService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class ListenerServiceImpl implements ListenerService {

    private final ListenerRegistry listenerRegistry;
    private UUID saveTaskUuid;

    @Inject
    public ListenerServiceImpl(final ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
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
//        if (!Config.isLoaded()) {
//            Cosmos.sendConsole(Text.of(TextColors.RED, "Configuration file not loaded. All listeners disabled !"));
//            return;
//        }

        this.listenerRegistry.entries().forEach(entry -> this.register(entry.getKey()));
        //.filter(entry -> Config.isListenerEnabled(entry.getKey()))
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
