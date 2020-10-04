package cosmos.listeners;

import com.google.common.base.Functions;
import cosmos.Cosmos;
import cosmos.listeners.perworld.AbstractPerWorldListener;
import cosmos.listeners.perworld.AdvancementsListener;
import cosmos.listeners.perworld.ChatsListener;
import cosmos.listeners.perworld.CommandBlocksListener;
import cosmos.listeners.perworld.ExperiencesListener;
import cosmos.listeners.perworld.GameModesListener;
import cosmos.listeners.perworld.HealthsListener;
import cosmos.listeners.perworld.HungersListener;
import cosmos.listeners.perworld.InventoriesListener;
import cosmos.listeners.perworld.ScoreboardsListener;
import cosmos.listeners.perworld.TabListsListener;
import cosmos.listeners.time.IgnorePlayersSleepingListener;
import cosmos.listeners.time.RealTimeListener;
import cosmos.statics.config.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListenerRegister {

    private static Map<Class<? extends AbstractListener>, AbstractListener> listenerMap = new HashMap<>();
    private static UUID scheduledSaveTaskUUID;

    public static void initializeListeners() {
        if (!Config.isLoaded()) {
            Cosmos.sendConsole(Text.of(TextColors.RED, "Configuration file not loaded. All listeners disabled !"));
            return;
        }

        listenerMap = Stream.of(
                new AdvancementsListener(),
                new ChatsListener(),
                new CommandBlocksListener(),
                new ExperiencesListener(),
                new GameModesListener(),
                new HealthsListener(),
                new HungersListener(),
                new IgnorePlayersSleepingListener(),
                new InventoriesListener(),
                new RealTimeListener(),
                new ScoreboardsListener(),
                new TabListsListener()
        ).collect(Collectors.toMap(AbstractListener::getClass, Functions.identity()));

        listenerMap.entrySet()
                .stream()
                .filter(entry -> Config.isListenerEnabled(entry.getKey()))
                .forEach(entry -> registerListener(entry.getKey()));
    }

    public static boolean isListenerRegistered(Class<? extends AbstractListener> listenerClass) {
        return listenerMap.get(listenerClass).isRegistered();
    }

    public static void registerListener(Class<? extends AbstractListener> listenerClass) {
        if (isListenerRegistered(listenerClass)) {
            return;
        }

        AbstractListener listener = listenerMap.get(listenerClass);

        if (listener instanceof AbstractPerWorldListener) {
            ((AbstractPerWorldListener) listener).onStart();
        }

        listener.setRegistered(true);
        Sponge.getEventManager().registerListeners(Cosmos.instance, listener);

        buildScheduledSaveTaskIfNotExists();
    }

    public static void unregisterListener(Class<? extends AbstractListener> listenerClass) {
        if (!isListenerRegistered(listenerClass)) {
            return;
        }

        AbstractListener listener = listenerMap.get(listenerClass);
        listener.setRegistered(false);
        Sponge.getEventManager().unregisterListeners(listener);

        if (listener instanceof AbstractPerWorldListener) {
            ((AbstractPerWorldListener) listener).onStop();
        }

        cancelScheduledSaveTaskIfEmpty();
    }

    public static void unregisterAll() {
        listenerMap.forEach((listenerClass, listener) -> Sponge.getEventManager().unregisterListeners(listener));
    }

    public static void cancelScheduledSaveTask() {
        if (scheduledSaveTaskUUID != null) {
            Sponge.getScheduler().getTaskById(scheduledSaveTaskUUID).ifPresent(Task::cancel);
        }
    }

    private static void cancelScheduledSaveTaskIfEmpty() {
        if (!hasScheduledSaveListenerRegistered()) {
            cancelScheduledSaveTask();
        }
    }

    private static boolean hasScheduledSaveListenerRegistered() {
        return listenerMap.values()
                .stream()
                .filter(listener -> listener instanceof ScheduledSaveListener)
                .anyMatch(AbstractListener::isRegistered);
    }

    private static void applyScheduledSave() {
        listenerMap.values().forEach(listener -> {
            if (!listener.isRegistered()) {
                return;
            }

            if (listener instanceof ScheduledAsyncSaveListener) {
                ((ScheduledAsyncSaveListener) listener).save();
            } else if (listener instanceof ScheduledSaveListener) {
                Task.builder().execute(((ScheduledSaveListener) listener)::save).submit(Cosmos.instance);
            }
        });
    }

    private static void buildScheduledSaveTaskIfNotExists() {
        if (scheduledSaveTaskUUID != null && Sponge.getScheduler().getTaskById(scheduledSaveTaskUUID).isPresent()) {
            return;
        }

        scheduledSaveTaskUUID = Task.builder()
                .execute(ListenerRegister::applyScheduledSave)
                .async()
                .delay(1, TimeUnit.MINUTES)
                .interval(1, TimeUnit.MINUTES)
                .submit(Cosmos.instance)
                .getUniqueId();
    }

}
