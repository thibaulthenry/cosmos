package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class WarningDistance extends AbstractBorderCommand {

    public WarningDistance() {
        super(Parameter.doubleNumber().setKey(CosmosKeys.DISTANCE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Double> optionalInput = context.getOne(CosmosKeys.DISTANCE);
        double value = border.getWarningDistance();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            border.setWarningDistance(value);
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.warning-distance.set" : "success.border.warning-distance.get")
                .replace("value", value)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
