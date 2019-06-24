package cosmos.commands;

import cosmos.Cosmos;
import cosmos.utils.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.function.Supplier;


public abstract class AbstractCommand implements CommandExecutor, ICommandSpec {

    protected String permission;

    public AbstractCommand(Permission permission) {
        this.permission = permission.toString();
    }

    public static String getName(Class clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    private void verifyPermissions(CommandSource src) throws CommandException {
        if (!src.hasPermission(permission)) {
            throw new CommandException(Text.of("Command not allowed !"));
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        verifyPermissions(src);
        return run(src, args);
    }

    protected abstract CommandResult run(CommandSource src, CommandContext args) throws CommandException;

    protected Supplier<CommandException> supplyError(String text) {
        return () -> new CommandException(Text.of(text));
    }

    protected void logError(Throwable errors, String... text) {
        Cosmos.getInstance().getLogger().error(String.join("", text), errors);
    }
}
