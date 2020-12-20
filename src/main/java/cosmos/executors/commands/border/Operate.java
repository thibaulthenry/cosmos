package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.models.enums.Operands;
import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import java.time.temporal.ChronoUnit;

@Singleton
public class Operate extends AbstractBorderCommand {

    public Operate() {
        super(
                CosmosParameters.STANDARD_OPERAND,
                Parameter.doubleNumber().setKey(CosmosKeys.AMOUNT).build(),
                Parameter.longNumber().setKey(CosmosKeys.DURATION).build(),
                CosmosParameters.TIME_UNIT_OPTIONAL
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties, final WorldBorder border) throws CommandException {
        final Operands operand = context.getOne(CosmosParameters.STANDARD_OPERAND)
                .orElseThrow(this.serviceProvider.message().getMessage(src, "error.invalid.operand").asSupplier());
        final double value = context.getOne(CosmosKeys.AMOUNT)
                .orElseThrow(this.serviceProvider.message().getMessage(src, "error.invalid.value").asSupplier());
        final long duration = context.getOne(CosmosKeys.DURATION).orElse(0L);
        final ChronoUnit unit = context.getOne(CosmosParameters.TIME_UNIT_OPTIONAL).orElse(ChronoUnit.SECONDS);

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
            throw this.serviceProvider.message().getMessage(src, "error.not-a-number").asException();
        }

        border.setDiameter(endDiameter, duration, unit);
        //this.serviceProvider.properties().save(properties); TODO Add in 1.16

        this.serviceProvider.message()
                .getMessage(src, "success.border.operate")
                .replace("world", properties)
                .replace("start_diameter", startDiameter)
                .replace("end_diameter", endDiameter)
                .replace("duration", duration)
                .replace("unit", unit)
                .successColor()
                .sendTo(src);
    }
}
