package cosmos.executors.commands.time;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.world.server.ServerWorldProperties;

@Singleton
public class RealTime extends AbstractTimeCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final MinecraftDayTime time) throws CommandException {

    }
}
