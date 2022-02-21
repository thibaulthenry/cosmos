package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.border.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Singleton
public class Transpose extends AbstractBorderCommand {

    public Transpose() {
        super(
                CosmosParameters.DURATION_WITH_UNIT.get().optional().build(),
                Parameter.doubleNumber().key(CosmosKeys.END_DIAMETER).build(),
                Parameter.doubleNumber().key(CosmosKeys.START_DIAMETER).optional().build()
        );
    }

    @Override
    protected List<String> additionalAliases() {
        return Collections.singletonList("move");
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final long duration = context.one(CosmosKeys.DURATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.DURATION));
        final ChronoUnit unit = context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);
        final double endDiameter = context.one(CosmosKeys.END_DIAMETER)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.END_DIAMETER));
        final double startDiameter = context.one(CosmosKeys.START_DIAMETER).orElse(border.diameter());

        // todo border.setDiameter(startDiameter, endDiameter, duration, unit);
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
