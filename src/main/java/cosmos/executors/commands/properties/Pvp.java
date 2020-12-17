package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

@Singleton
public class Pvp extends AbstractPropertiesCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties) throws CommandException {

    }
}
