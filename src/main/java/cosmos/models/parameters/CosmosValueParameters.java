package cosmos.models.parameters;

import cosmos.models.enums.Operands;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

import java.time.temporal.ChronoUnit;

class CosmosValueParameters {

    static final ValueParameter<Operands> STANDARD_OPERANDS = VariableValueParameters.staticChoicesBuilder(Operands.class)
            .choice(Operands.PLUS.getOperand(), Operands.PLUS)
            .choice(Operands.MINUS.getOperand(), Operands.MINUS)
            .choice(Operands.TIMES.getOperand(), Operands.TIMES)
            .choice(Operands.DIVIDE.getOperand(), Operands.DIVIDE)
            .build();

    static final ValueParameter<ChronoUnit> TIME_UNIT = VariableValueParameters.staticChoicesBuilder(ChronoUnit.class)
            .choice(ChronoUnit.MILLIS.toString(), ChronoUnit.MILLIS)
            .choice(ChronoUnit.SECONDS.toString(), ChronoUnit.SECONDS)
            .choice(ChronoUnit.MINUTES.toString(), ChronoUnit.MINUTES)
            .choice(ChronoUnit.HOURS.toString(), ChronoUnit.HOURS)
            .choice(ChronoUnit.DAYS.toString(), ChronoUnit.DAYS)
            .choice(ChronoUnit.WEEKS.toString(), ChronoUnit.WEEKS)
            .choice(ChronoUnit.MONTHS.toString(), ChronoUnit.MONTHS)
            .choice(ChronoUnit.YEARS.toString(), ChronoUnit.YEARS)
            .build();
}
