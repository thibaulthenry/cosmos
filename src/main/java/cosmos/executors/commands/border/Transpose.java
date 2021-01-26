package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Singleton
public class Transpose extends AbstractBorderCommand {

    public Transpose() {
        super(
                CosmosParameters.DURATION_WITH_TIME_UNIT_OPTIONAL,
                Parameter.doubleNumber().setKey(CosmosKeys.END_DIAMETER).build(),
                Parameter.doubleNumber().setKey(CosmosKeys.START_DIAMETER).optional().build()
        );
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("move");
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final long duration = context.getOne(CosmosKeys.DURATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.DURATION));
        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);
        final double endDiameter = context.getOne(CosmosKeys.END_DIAMETER)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.END_DIAMETER));
        final double startDiameter = context.getOne(CosmosKeys.START_DIAMETER).orElse(border.getDiameter());

        border.setDiameter(startDiameter, endDiameter, duration, unit);
        super.serviceProvider.world().saveProperties(src, properties);

        super.serviceProvider.message()
                .getMessage(src, "success.border.transpose")
                .replace("duration", duration)
                .replace("end_diameter", endDiameter)
                .replace("start_diameter", startDiameter)
                .replace("unit", unit)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
