package cosmos.listeners.perworld;

import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

public class CommandBlocksListener extends AbstractPerWorldListener {

    @Listener(order = Order.FIRST)
    public void onSentCommand(SendCommandEvent event, @First CommandBlockSource source) {
        if (!source.getLocation().getExtent().getProperties().areCommandsAllowed()) {
            event.setCancelled(true);
        }
    }

}
