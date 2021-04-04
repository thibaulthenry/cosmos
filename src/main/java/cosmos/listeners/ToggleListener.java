package cosmos.listeners;

import org.spongepowered.api.entity.living.player.Player;

public interface ToggleListener {

    default void onStart() {
    }

    default void onStart(Player player) {
    }

    default void onStop() {
    }

    default void onStop(Player player) {
    }
}
