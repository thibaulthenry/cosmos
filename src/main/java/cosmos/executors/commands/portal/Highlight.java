package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

@Singleton
public class Highlight extends AbstractCommand {

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        super.serviceProvider.portal().highlight(src, Ticks.of(599)); // todo
    }

}
