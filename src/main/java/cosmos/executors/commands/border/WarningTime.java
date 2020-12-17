package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Singleton
public class WarningTime extends AbstractBorderCommand {

    public WarningTime() {
        super(
                Parameter.longNumber().setKey(CosmosKeys.DURATION).optional().build(),
                Parameter.enumValue(ChronoUnit.class).setKey(CosmosKeys.TEMPORAL_UNIT).orDefault(ChronoUnit.SECONDS).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Long> optionalInput = context.getOne(CosmosKeys.DURATION);
        Duration value = border.getWarningTime();

        if (optionalInput.isPresent()) {
            value = Duration.of(optionalInput.get(), ChronoUnit.SECONDS);
            border.setWarningTime(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.warning-time.set" : "success.border.warning-time.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
