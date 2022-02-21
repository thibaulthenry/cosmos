package cosmos.registries.listener;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public interface ToggleListener extends Listener {

    default void start() {}

    default void start(final ServerPlayer player) {}

    default void stop() {}

    default void stop(final ServerPlayer player) {}

}
