package cosmos.executors.commands.time;

import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

public abstract class AbstractTimeChangeCommand extends AbstractTimeCommand {

    protected AbstractTimeChangeCommand(Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final MinecraftDayTime time) throws CommandException {
        final long ticks = Math.max(0, getNewTime(src, context, time));
        final MinecraftDayTime newTime = MinecraftDayTime.of(Sponge.getServer(), Ticks.of(ticks));

        properties.setDayTime(newTime);

        this.serviceProvider.world().saveProperties(src, properties);

        this.serviceProvider.message()
                .getMessage(src, "success.time.set")
                .replace("world", properties)
                .replace("value", ticks)
                .replace("day", this.serviceProvider.time().fromWorldTimeToDayNumber(ticks))
                .replace("hour", this.serviceProvider.time().fromWorldTimeToDayWatch(ticks))
                .green()
                .sendTo(src);
    }

    protected abstract long getNewTime(Audience src, CommandContext context, MinecraftDayTime time) throws CommandException;
}
