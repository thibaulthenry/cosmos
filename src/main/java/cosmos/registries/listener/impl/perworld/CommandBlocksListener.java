package cosmos.registries.listener.impl.perworld;

import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.selector.Selector;
import org.spongepowered.api.command.selector.SelectorType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class CommandBlocksListener extends AbstractPerWorldListener {

    @Listener(order = Order.FIRST)
    public void onPreExecuteCommandEvent(final ExecuteCommandEvent.Pre event, @First final CommandCause cause) {
        if (cause.getLocation().isPresent() && !cause.getLocation().get().getWorld().getProperties().commands()) {
            event.setCancelled(true);
        }
    }

}
