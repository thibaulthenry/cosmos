package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.constants.Operands;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.time.temporal.ChronoUnit;

@Singleton
public class Operate extends AbstractBorderCommand {

    public Operate() {
        super(
                CosmosParameters.STANDARD_OPERAND,
                Parameter.doubleNumber().setKey(CosmosKeys.AMOUNT_DOUBLE).build(),
                CosmosParameters.DURATION_WITH_TIME_UNIT_OPTIONAL
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Operands operand = context.getOne(CosmosParameters.STANDARD_OPERAND)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.OPERAND));
        final double value = context.getOne(CosmosKeys.AMOUNT_DOUBLE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.AMOUNT_DOUBLE));
        final long duration = context.getOne(CosmosKeys.DURATION).orElse(0L);
        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        final double startDiameter = border.getDiameter();
        final double endDiameter;

        switch (operand) {
            case MINUS:
                endDiameter = startDiameter - value;
                break;
            case TIMES:
                endDiameter = startDiameter * value;
                break;
            case DIVIDE:
                endDiameter = startDiameter / value;
                break;
            default:
                endDiameter = startDiameter + value;
                break;
        }

        if (Double.isNaN(endDiameter) || Double.isInfinite(endDiameter)) {
            throw super.serviceProvider.message().getError(src, "error.result.nan");
        }

        border.setDiameter(endDiameter, duration, unit);
        super.serviceProvider.world().saveProperties(src, properties);

        super.serviceProvider.message()
                .getMessage(src, "success.border.operate")
                .replace("duration", duration)
                .replace("end_diameter", endDiameter)
                .replace("start_diameter", startDiameter)
                .replace("unit", unit)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
