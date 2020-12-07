package cosmos.executors.commands;

import cosmos.Cosmos;
import cosmos.executors.AbstractExecutor;
import cosmos.services.ServiceProvider;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public abstract class AbstractCommand extends AbstractExecutor {

    protected final ServiceProvider serviceProvider;
    private final String permission;
    private final Parameter[] parameters;
    private Command.Parameterized parameterized;

    protected AbstractCommand(final Parameter... parameters) {
        final String commandName = this.getClass().getSimpleName().toLowerCase();

        this.parameters = parameters;
        this.permission = this.getClass().getPackage().getName() + "." + commandName;
        this.serviceProvider = Cosmos.getServices();
    }

    @Override
    public CommandResult execute(final Audience audience, final CommandContext context) throws CommandException {
        this.run(context.getCause().getAudience(), context);

        return CommandResult.success();
    }

    protected abstract void run(final Audience audience, final CommandContext context) throws CommandException;

    @Override
    public Command.Parameterized getParametrized() {
        if (this.parameterized != null) {
            return this.parameterized;
        }

        this.parameterized = Command.builder()
                .parameters(this.parameters)
                .setPermission(this.permission)
                .setExecutor(this)
                .build();

        return parameterized;
    }

    public String getPermission() {
        return this.permission;
    }
}