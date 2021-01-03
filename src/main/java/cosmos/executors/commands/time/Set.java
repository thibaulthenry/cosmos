package cosmos.executors.commands.time;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.util.MinecraftDayTime;

@Singleton
public class Set extends AbstractTimeChangeCommand {

    public Set() {
        super(Parameter.longNumber().setKey(CosmosKeys.TICKS).build());
    }

    @Override
    protected long getNewTime(final Audience src, final CommandContext context, final MinecraftDayTime time) throws CommandException {
        return context.getOne(CosmosKeys.TICKS).orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.duration.ticks"));
    }

}
