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

@Singleton
public class Transpose extends AbstractBorderCommand {

    public Transpose() {
        super(
                Parameter.longNumber().setKey(CosmosKeys.DURATION).build(),
                Parameter.enumValue(ChronoUnit.class).setKey(CosmosKeys.TEMPORAL_UNIT).optional().build(),
                Parameter.doubleNumber().setKey(CosmosKeys.END_DIAMETER).build(),
                Parameter.doubleNumber().setKey(CosmosKeys.START_DIAMETER).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties, final WorldBorder border) throws CommandException {
        final long duration = context.getOne(CosmosKeys.DURATION)
                .orElseThrow(this.serviceProvider.message().getMessage(src, "error.invalid.duration").asSupplier());
        final ChronoUnit unit = context.getOne(CosmosKeys.TEMPORAL_UNIT).orElse(ChronoUnit.MILLIS);
        final double endDiameter = context.getOne(CosmosKeys.END_DIAMETER)
                .orElseThrow(this.serviceProvider.message().getMessage(src, "error.invalid.diameter").asSupplier());
        final double startDiameter = context.getOne(CosmosKeys.START_DIAMETER).orElse(border.getDiameter());

        border.setDiameter(startDiameter, endDiameter, duration, unit);
        //this.serviceProvider.properties().save(properties); TODO Add in 1.16

        this.serviceProvider.message()
                .getMessage(src, "success.border.transpose")
                .replace("world", properties)
                .replace("start_diameter", startDiameter)
                .replace("end_diameter", endDiameter)
                .replace("duration", Duration.of(duration, unit))
                .successColor()
                .sendTo(src);
    }
}
