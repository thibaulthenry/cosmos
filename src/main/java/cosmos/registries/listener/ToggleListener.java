package cosmos.registries.listener;

public interface ToggleListener extends Listener {

    default void start() {}

    default void stop() {}
}
