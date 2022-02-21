package cosmos.services.listener;

import com.google.inject.ImplementedBy;
import cosmos.registries.listener.Listener;
import cosmos.services.CosmosService;
import cosmos.services.listener.impl.ListenerServiceImpl;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

@ImplementedBy(ListenerServiceImpl.class)
public interface ListenerService extends CosmosService {

    void cancelSaveTaskIfNot();

    String format(Class<? extends Listener> listenerClass);

    boolean isRegisteredToSponge(Class<? extends Listener> clazz);

    void submitSaveTaskIfNot();

    void toggle(Class<? extends Listener> listenerClass, boolean state);

    void toggle(Class<? extends Listener> listenerClass, boolean state, @Nullable ServerPlayer player);

}
