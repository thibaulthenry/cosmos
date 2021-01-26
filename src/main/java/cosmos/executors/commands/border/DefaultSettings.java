package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.time.temporal.ChronoUnit;

@Singleton
public class DefaultSettings extends AbstractBorderCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        border.setCenter(0, 0);
        border.setDamageAmount(0.2);
        border.setDamageThreshold(5);
        border.setDiameter(Double.POSITIVE_INFINITY);
        border.setWarningDistance(5);
        border.setWarningTime(15, ChronoUnit.SECONDS);

        super.serviceProvider.world().saveProperties(src, properties);

        super.serviceProvider.message()
                .getMessage(src, "success.border.default-settings")
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
