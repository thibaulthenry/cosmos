package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Collections;
import java.util.List;

@Singleton
public class Position extends AbstractCommand {

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("pos");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {

    }

}
