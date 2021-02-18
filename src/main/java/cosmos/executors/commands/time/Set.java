package cosmos.executors.commands.time;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.util.MinecraftDayTime;

@Singleton
public class Set extends AbstractTimeModifyCommand {

    public Set() {
        super(Parameter.longNumber().key(CosmosKeys.TICKS).build());
    }

    @Override
    protected long newTime(final Audience src, final CommandContext context, final MinecraftDayTime time) throws CommandException {
        return context.one(CosmosKeys.TICKS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.TICKS));
    }

}
