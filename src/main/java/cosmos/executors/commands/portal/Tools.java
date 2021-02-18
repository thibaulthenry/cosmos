package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

@Singleton
public class Tools extends AbstractCommand {

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        if (!(src instanceof ServerPlayer)) {
            return;
        }

        final ServerPlayer player = (ServerPlayer) src;

        Sponge.getServer().getCommandManager().process(player, "minecraft:give " + player.getName() + " minecraft:barrier");
        Sponge.getServer().getCommandManager().process(player, "minecraft:give " + player.getName() + " minecraft:flint_and_steel");

        // todo message use barrier and right click
    }

}
