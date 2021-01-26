package cosmos.executors.commands;

import cosmos.executors.AbstractExecutor;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.Flag;

public abstract class AbstractCommand extends AbstractExecutor {

    private final Flag[] flags;
    private final Parameter[] parameters;
    private final String permission;

    private Command.Parameterized parameterized;
    private int successCount;

    protected AbstractCommand(final Parameter... parameters) {
        final String commandName = this.getClass().getSimpleName().toLowerCase();
        this.flags = this.flags();
        this.parameters = parameters;
        this.permission = this.getClass().getPackage().getName().replace(".executors", "") + "." + commandName;
    }

    @Override
    public final CommandResult execute(final Audience src, final CommandContext context) throws CommandException {
        this.successCount = this.initializeSuccessCount();
        this.run(context.getCause().getAudience(), context);

        return CommandResult.builder().setResult(this.successCount).build();
    }

    protected Flag[] flags() {
        return new Flag[0];
    }

    @Override
    public Command.Parameterized getParametrized() {
        if (this.parameterized != null) {
            return this.parameterized;
        }

        this.parameterized = Command.builder()
                // todo
                .parameters(this.parameters)
                .setPermission(this.permission)
                .setExecutor(this)
                .build();

        return this.parameterized;
    }

    public String getPermission() {
        return this.permission;
    }

    protected int initializeSuccessCount() {
        return 1;
    }

    protected abstract void run(Audience src, CommandContext context) throws CommandException;

    protected void success() {
        this.success(1);
    }

    protected void success(int successAmount) {
        this.successCount += successAmount;
    }

}
