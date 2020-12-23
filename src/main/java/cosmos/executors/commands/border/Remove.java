package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.ServerWorldProperties;

@Singleton
public class Remove extends AbstractBorderCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        border.setDiameter(Double.POSITIVE_INFINITY);
        this.serviceProvider.worldProperties().save(properties);

        this.serviceProvider.message()
                .getMessage(src, "success.border.remove")
                .replace("world", properties)
                .successColor()
                .sendTo(src);
    }
}
