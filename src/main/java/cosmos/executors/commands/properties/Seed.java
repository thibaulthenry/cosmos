package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorldProperties;

@Singleton
public class Seed extends AbstractPropertiesCommand{

    public Seed() {
        super();
    }

    @Override
    protected void run(Audience src, CommandContext context, ServerWorldProperties properties) throws CommandException {

    }
}
