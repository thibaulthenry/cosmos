package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.util.Tristate;

@Singleton
public class CommandBlocksListener extends AbstractPerWorldListener {

    @Listener(order = Order.FIRST)
    @IsCancelled(Tristate.FALSE)
    public void onPreExecuteCommandEvent(final ExecuteCommandEvent.Pre event, @First final CommandCause cause) {
        if (cause.location().isPresent() && !cause.location().get().world().properties().commands()) {
            event.setCancelled(true);
        }
    }

}
