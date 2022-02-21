package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.border.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

@Singleton
public class DefaultSettings extends AbstractBorderCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        // todo border.setCenter(0, 0);
        // todo border.setDamageAmount(0.2);
        // todo border.setDamageThreshold(5);
        // todo border.setDiameter(Double.POSITIVE_INFINITY);
        // todo border.setWarningDistance(5);
        // todo border.setWarningTime(15, ChronoUnit.SECONDS);
        super.serviceProvider.world().saveProperties(src, properties);

        super.serviceProvider.message()
                .getMessage(src, "success.border.default-settings")
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
