package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameterModifier;
import org.spongepowered.api.command.parameter.managed.operator.Operator;
import org.spongepowered.api.command.parameter.managed.operator.Operators;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.world.border.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Operate extends AbstractBorderCommand {

    public Operate() {
        super(
                Parameter.builder(Operator.class)
                        .addParser(ResourceKeyedValueParameters.OPERATOR)
                        .modifier(new OperatorModifier())
                        .key(CosmosKeys.OPERATOR)
                        .build(),
                Parameter.doubleNumber().key(CosmosKeys.AMOUNT_DOUBLE).build(),
                CosmosParameters.DURATION_WITH_UNIT.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Operator.Simple operator = context.one(CosmosKeys.OPERATOR)
                .filter(value -> value instanceof Operator.Simple)
                .map(value -> (Operator.Simple) value)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.OPERATOR));
        final double value = context.one(CosmosKeys.AMOUNT_DOUBLE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.AMOUNT_DOUBLE));
        final long duration = context.one(CosmosKeys.DURATION).orElse(0L);
        final ChronoUnit unit = context.one(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        final double startDiameter = border.diameter();
        final double endDiameter = operator.apply(startDiameter, value);

        if (Double.isNaN(endDiameter) || Double.isInfinite(endDiameter)) {
            throw super.serviceProvider.message().getError(src, "error.result.nan");
        }

        // todo border.setDiameter(endDiameter, duration, unit);
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

    private static class OperatorModifier implements ValueParameterModifier<Operator> {

        @Override
        public Optional<? extends Operator> modifyResult(final Parameter.Key<? super Operator> parameterKey, final ArgumentReader.Immutable reader, final CommandContext.Builder context, @Nullable final Operator value) {
            return Optional.ofNullable(value);
        }

        @Override
        public List<CommandCompletion> modifyCompletion(CommandContext context, String currentInput, List<CommandCompletion> completions) {
            return completions
                    .stream()
                    .map(CommandCompletion::completion)
                    .filter(operator -> Operators.ADDITION.get().asString().equals(operator)
                            || Operators.SUBTRACTION.get().asString().equals(operator)
                            || Operators.MULTIPLICATION.get().asString().equals(operator)
                            || Operators.DIVISION.get().asString().equals(operator))
                    .map(CommandCompletion::of)
                    .collect(Collectors.toList());
        }

    }

}
