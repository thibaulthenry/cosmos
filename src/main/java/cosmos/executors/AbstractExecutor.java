package cosmos.executors;

import cosmos.Cosmos;
import cosmos.services.ServiceProvider;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;


public abstract class AbstractExecutor implements CommandExecutor {

    protected final ServiceProvider serviceProvider;

    protected AbstractExecutor() {
        this.serviceProvider = Cosmos.getServices();
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        return this.execute(context.getCause().getAudience(), context);
    }

    protected abstract CommandResult execute(final Audience src, final CommandContext context) throws CommandException;

    public abstract Command.Parameterized getParametrized();
}
