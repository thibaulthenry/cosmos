package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Back extends AbstractCommand {

    public Back() {
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
    }

}