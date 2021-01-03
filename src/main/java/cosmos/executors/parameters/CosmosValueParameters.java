package cosmos.executors.parameters;

import cosmos.constants.Operands;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

import java.time.temporal.ChronoUnit;

class CosmosValueParameters {

    static final ValueParameter<Operands> SCOREBOARD_OPERANDS = VariableValueParameters.staticChoicesBuilder(Operands.class)
            .choice(Operands.PLUS.getOperand(), Operands.PLUS)
            .choice(Operands.MINUS.getOperand(), Operands.MINUS)
            .choice(Operands.TIMES.getOperand(), Operands.TIMES)
            .choice(Operands.DIVIDE.getOperand(), Operands.DIVIDE)
            .choice(Operands.MODULUS.getOperand(), Operands.MODULUS)
            .choice(Operands.ASSIGN.getOperand(), Operands.ASSIGN)
            .choice(Operands.MIN.getOperand(), Operands.MIN)
            .choice(Operands.MAX.getOperand(), Operands.MAX)
            .choice(Operands.SWAPS.getOperand(), Operands.SWAPS)
            .build();

    static final ValueParameter<Operands> STANDARD_OPERANDS = VariableValueParameters.staticChoicesBuilder(Operands.class)
            .choice(Operands.PLUS.getOperand(), Operands.PLUS)
            .choice(Operands.MINUS.getOperand(), Operands.MINUS)
            .choice(Operands.TIMES.getOperand(), Operands.TIMES)
            .choice(Operands.DIVIDE.getOperand(), Operands.DIVIDE)
            .build();

    static final ValueParameter<ChronoUnit> TIME_UNIT = VariableValueParameters.staticChoicesBuilder(ChronoUnit.class)
            .choice("milliseconds", ChronoUnit.MILLIS)
            .choice("seconds", ChronoUnit.SECONDS)
            .choice("minutes", ChronoUnit.MINUTES)
            .choice("hours", ChronoUnit.HOURS)
            .choice("days", ChronoUnit.DAYS)
            .choice("weeks", ChronoUnit.WEEKS)
            .choice("months", ChronoUnit.MONTHS)
            .choice("years", ChronoUnit.YEARS)
            .build();
}
