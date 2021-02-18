package cosmos.executors.commands;

import cosmos.executors.AbstractExecutor;
import net.kyori.adventure.audience.Audience;
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

    protected void addSuccess() {
        this.addSuccess(1);
    }

    protected void addSuccess(final int successAmount) {
        this.successCount += successAmount;
    }

    @Override
    public final CommandResult execute(final Audience src, final CommandContext context) throws CommandException {
        this.successCount = this.initializeSuccessCount();
        this.run(context.cause().audience(), context);

        return CommandResult.builder().result(this.successCount).build();
    }

    protected Flag[] flags() {
        return new Flag[0];
    }

    protected int initializeSuccessCount() {
        return 1;
    }

    @Override
    public Command.Parameterized parametrized() {
        if (this.parameterized != null) {
            return this.parameterized;
        }

        this.parameterized = Command.builder()
                .addFlags(this.flags)
                .addParameters(this.parameters)
                .executor(this)
                .permission(this.permission)
                .build();

        return this.parameterized;
    }

    public String permission() {
        return this.permission;
    }

    protected abstract void run(Audience src, CommandContext context) throws CommandException;

}
