package cosmos.executors.commands.weather;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Forecast extends AbstractCommand {
    @Override
    protected void run(Audience audience, CommandContext context) throws CommandException {

    }
}