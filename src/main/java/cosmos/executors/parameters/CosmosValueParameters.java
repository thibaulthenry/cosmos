package cosmos.executors.parameters;

import cosmos.constants.Operands;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;
import org.spongepowered.api.world.gamerule.GameRule;

import java.time.temporal.ChronoUnit;
import java.util.List;

class CosmosValueParameters {

    static final ValueParameter<NamedTextColor> COLORS = VariableValueParameters.staticChoicesBuilder(NamedTextColor.class)
            .choice(NamedTextColor.AQUA.toString(), NamedTextColor.AQUA)
            .choice(NamedTextColor.BLACK.toString(), NamedTextColor.BLACK)
            .choice(NamedTextColor.BLUE.toString(), NamedTextColor.BLUE)
            .choice(NamedTextColor.DARK_AQUA.toString(), NamedTextColor.DARK_AQUA)
            .choice(NamedTextColor.DARK_BLUE.toString(), NamedTextColor.DARK_BLUE)
            .choice(NamedTextColor.DARK_GRAY.toString(), NamedTextColor.DARK_GRAY)
            .choice(NamedTextColor.DARK_GREEN.toString(), NamedTextColor.DARK_GREEN)
            .choice(NamedTextColor.DARK_PURPLE.toString(), NamedTextColor.DARK_PURPLE)
            .choice(NamedTextColor.DARK_RED.toString(), NamedTextColor.DARK_RED)
            .choice(NamedTextColor.GOLD.toString(), NamedTextColor.GOLD)
            .choice(NamedTextColor.GRAY.toString(), NamedTextColor.GRAY)
            .choice(NamedTextColor.GREEN.toString(), NamedTextColor.GREEN)
            .choice(NamedTextColor.LIGHT_PURPLE.toString(), NamedTextColor.LIGHT_PURPLE)
            .choice(NamedTextColor.RED.toString(), NamedTextColor.RED)
            .choice("reset", NamedTextColor.WHITE)
            .choice(NamedTextColor.WHITE.toString(), NamedTextColor.WHITE)
            .choice(NamedTextColor.YELLOW.toString(), NamedTextColor.YELLOW)
            .build();

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
