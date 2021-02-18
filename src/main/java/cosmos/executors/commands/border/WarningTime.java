package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Singleton
public class WarningTime extends AbstractBorderCommand {

    public WarningTime() {
        super(CosmosParameters.DURATION_WITH_UNIT.get().optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Long> optionalInput = context.one(CosmosKeys.DURATION);
        Duration value = border.warningTime();

        if (optionalInput.isPresent()) {
            value = Duration.of(optionalInput.get(), context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS));
            border.setWarningTime(value);
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.warning-time.set" : "success.border.warning-time.get")
                .replace("value", value)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
