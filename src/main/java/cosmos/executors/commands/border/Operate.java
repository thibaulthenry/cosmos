package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.Operands;
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
                CosmosParameters.STANDARD_OPERAND.get().key(CosmosKeys.OPERAND).build(),
                Parameter.doubleNumber().key(CosmosKeys.AMOUNT_DOUBLE).build(),
                CosmosParameters.DURATION_WITH_UNIT.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Operands operand = context.one(CosmosKeys.OPERAND)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.OPERAND));
        final double value = context.one(CosmosKeys.AMOUNT_DOUBLE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.AMOUNT_DOUBLE));
        final long duration = context.one(CosmosKeys.DURATION).orElse(0L);
        final ChronoUnit unit = context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        final double startDiameter = border.diameter();
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
