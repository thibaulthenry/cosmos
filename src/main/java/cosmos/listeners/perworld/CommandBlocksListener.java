package cosmos.listeners.perworld;

import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.PerWorldCommands;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;

import java.util.Collections;

public class CommandBlocksListener extends AbstractPerWorldListener {

    @Listener(order = Order.FIRST)
    public void onSentCommand(SendCommandEvent event, @First CommandBlockSource source) {
        World world = source.getLocation().getExtent();

        String groupedWorldName = GroupRegister.find(Tuple.of(PerWorldCommands.COMMAND_BLOCKS, world.getName()))
                .orElse(Collections.singleton(world.getName()))
                .iterator()
                .next();

        world = Sponge.getServer().getWorld(groupedWorldName).orElse(world);

        if (!world.getProperties().areCommandsAllowed()) {
            event.setCancelled(true);
        }
    }

}
