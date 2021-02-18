package cosmos.executors;

import com.google.inject.Inject;
import cosmos.services.ServiceProvider;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class AbstractExecutor implements CommandExecutor {

    @Inject
    protected ServiceProvider serviceProvider;

    private final List<String> aliases = new ArrayList<>();

    protected AbstractExecutor() {
        this.aliases.add(this.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        this.aliases.addAll(this.additionalAliases());
    }

    protected List<String> additionalAliases() {
        return Collections.emptyList();
    }

    public List<String> aliases() {
        return this.aliases;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        return this.execute(context.cause().audience(), context);
    }

    protected abstract CommandResult execute(Audience src, CommandContext context) throws CommandException;

    public abstract Command.Parameterized parametrized();

}
