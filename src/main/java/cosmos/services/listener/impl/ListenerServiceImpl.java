package cosmos.services.listener.impl;

import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.listener.Listener;
import cosmos.registries.listener.ListenerRegistry;
import cosmos.registries.listener.ScheduledAsyncSaveListener;
import cosmos.registries.listener.ScheduledSaveListener;
import cosmos.registries.listener.ToggleListener;
import cosmos.services.listener.ListenerService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class ListenerServiceImpl implements ListenerService {

    private final ListenerRegistry listenerRegistry;

    private UUID saveTaskUuid;

    @Inject
    public ListenerServiceImpl(final Injector injector) {
        this.listenerRegistry = injector.getInstance(ListenerRegistry.class);
    }

    @Override
    public String format(final Class<? extends Listener> listenerClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, listenerClass.getSimpleName().replace("Listener", ""));
    }

    @Override
    public boolean isRegisteredToSponge(final Class<? extends Listener> clazz) {
        return this.listenerRegistry.find(clazz).map(Listener::registeredToSponge).orElse(false);
    }

    @Override
    public void cancelSaveTaskIfNot() {
        if (this.saveTaskUuid != null) {
            Sponge.asyncScheduler().findTask(this.saveTaskUuid).ifPresent(ScheduledTask::cancel);
            this.saveTaskUuid = null;
        }
    }

    private void saveTaskExecutor() {
        this.listenerRegistry.stream().forEach(listener -> {
            if (!listener.registeredToSponge()) {
                return;
            }

            if (listener instanceof ScheduledAsyncSaveListener) {
                ((ScheduledAsyncSaveListener) listener).save();
            } else if (listener instanceof ScheduledSaveListener) {
                final Task task = Task.builder()
                        .execute(((ScheduledSaveListener) listener)::save)
                        .plugin(Cosmos.pluginContainer())
                        .build();

                this.saveTaskUuid = Sponge.server().scheduler().submit(task).uniqueId();
            }
        });
    }

    @Override
    public void submitSaveTaskIfNot() {
        if (this.saveTaskUuid != null && Sponge.asyncScheduler().findTask(this.saveTaskUuid).isPresent()) {
            return;
        }

        final Task task = Task.builder()
                .execute(this::saveTaskExecutor)
                .delay(1, TimeUnit.MINUTES)
                .interval(1, TimeUnit.MINUTES)
                .plugin(Cosmos.pluginContainer())
                .build();

        this.saveTaskUuid = Sponge.asyncScheduler().submit(task).uniqueId();
    }

    @Override
    public void toggle(final Class<? extends Listener> listenerClass, final boolean state) {
        this.toggle(listenerClass, state, null);
    }

    @Override
    public void toggle(final Class<? extends Listener> listenerClass, final boolean state, @Nullable final ServerPlayer player) {
        this.listenerRegistry.find(listenerClass)
                .filter(listener -> listener.registeredToSponge() && listener instanceof ToggleListener)
                .map(listener -> (ToggleListener) listener)
                .ifPresent(listener -> {
                    if (player == null) {
                        if (state) {
                            listener.start();
                        } else {
                            listener.stop();
                        }
                    } else {
                        if (state) {
                            listener.start(player);
                        } else {
                            listener.stop(player);
                        }
                    }
                });
    }

}
