package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class CommandBlocksListener extends AbstractListener implements ToggleListener {

    @Listener(order = Order.FIRST)
    public void onPreExecuteCommandEvent(final ExecuteCommandEvent.Pre event, @First final CommandCause cause) {
        if (cause.getLocation().isPresent() && !cause.getLocation().get().getWorld().getProperties().commands()) {
            event.setCancelled(true);
        }
    }

}
