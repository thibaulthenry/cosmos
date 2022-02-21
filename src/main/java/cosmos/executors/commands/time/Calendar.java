package cosmos.executors.commands.time;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

@Singleton
public class Calendar extends AbstractTimeCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final MinecraftDayTime time) throws CommandException {
        final long ticks = time.asTicks().ticks();

        super.serviceProvider.message()
                .getMessage(src, "success.time.get")
                .replace("day", super.serviceProvider.time().fromWorldTimeToDayNumber(ticks))
                .replace("hour", super.serviceProvider.time().fromWorldTimeToDayWatch(ticks))
                .replace("value", ticks)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}