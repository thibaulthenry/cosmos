package cosmos.executors.commands.time;

import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

abstract class AbstractTimeChangeCommand extends AbstractTimeCommand {

    AbstractTimeChangeCommand(Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final MinecraftDayTime time) throws CommandException {
        final long ticks = Math.max(0, getNewTime(src, context, time));
        final MinecraftDayTime newTime = MinecraftDayTime.of(Sponge.getServer(), Ticks.of(ticks));

        properties.setDayTime(newTime);

        super.serviceProvider.world().saveProperties(src, properties);

        super.serviceProvider.message()
                .getMessage(src, "success.time.set")
                .replace("day", super.serviceProvider.time().fromWorldTimeToDayNumber(ticks))
                .replace("hour", super.serviceProvider.time().fromWorldTimeToDayWatch(ticks))
                .replace("value", ticks)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

    protected abstract long getNewTime(Audience src, CommandContext context, MinecraftDayTime time) throws CommandException;

}
