package cosmos.executors.commands.perworld;

import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.registries.listener.impl.perworld.AbstractPerWorldListener;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Group extends AbstractPerWorldCommand {

    public Group() {
        super(CosmosParameters.WORLD_DISTINCT.get().consumeAllRemaining().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final AbstractPerWorldListener listener) throws CommandException {

    }

}
