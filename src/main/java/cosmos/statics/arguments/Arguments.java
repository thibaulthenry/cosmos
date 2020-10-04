package cosmos.statics.arguments;

import cosmos.constants.ArgKeys;
import cosmos.constants.Helps;
import cosmos.constants.Operands;
import cosmos.constants.PerWorldCommands;
import cosmos.statics.arguments.implementations.EnhancedCatalogedElement;
import cosmos.statics.arguments.implementations.EnhancedCommandElement;
import cosmos.statics.arguments.implementations.FirstParsingElement;
import cosmos.statics.arguments.implementations.LimitCompleteElement;
import cosmos.statics.arguments.implementations.Vector2dElement;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.CommandFlags;
import org.spongepowered.api.command.args.GenericArguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Arguments {

    private static final int DEFAULT_COMPLETE_LIMIT = 20;

    public static CommandElement baseFlag(ArgKeys argName) {
        return GenericArguments.flags()
                .flag(argName.f)
                .setUnknownShortFlagBehavior(CommandFlags.UnknownFlagBehavior.IGNORE)
                .setUnknownLongFlagBehavior(CommandFlags.UnknownFlagBehavior.IGNORE)
                .buildWith(GenericArguments.none());
    }

    public static <T extends CatalogType> EnhancedCommandElement enhancedCatalogedElement(ArgKeys argName, Class<T> catalogType) {
        return new EnhancedCatalogedElement<>(argName.t, catalogType);
    }

    static EnhancedCommandElement firstParsing(ArgKeys argName, boolean optionalUsage, CommandElement... commandElements) {
        return new FirstParsingElement(argName.t, optionalUsage, commandElements);
    }

    static EnhancedCommandElement firstParsing(ArgKeys argName, CommandElement... commandElements) {
        return firstParsing(argName, false, commandElements);
    }

    public static CommandElement gameRuleChoices(ArgKeys argName) {
        return ChoicesArguments.collectionChoices(
                argName,
                () -> Sponge.getRegistry().getDefaultGameRules(),
                false);
    }

    public static CommandElement helpChoices(ArgKeys argName) {
        return ChoicesArguments.choices(
                argName,
                Arguments::toHelpsMap,
                false);
    }

    private static CommandElement limitCompleteElement(CommandElement element, int limit) {
        return new LimitCompleteElement(element, limit);
    }

    public static CommandElement limitCompleteElement(CommandElement element) {
        return limitCompleteElement(element, DEFAULT_COMPLETE_LIMIT);
    }

    public static CommandElement operandChoices(ArgKeys argName, boolean showOperand, Operands... filters) {
        return ChoicesArguments.choices(
                argName,
                () -> toOperandsMap(showOperand, filters),
                true);
    }

    public static CommandElement perWorldChoices(ArgKeys argName) {
        return ChoicesArguments.choices(
                argName,
                Arguments::toPerWorldCommandsMap,
                true);
    }

    public static CommandElement vector2d(ArgKeys argName) {
        return new Vector2dElement(argName.t);
    }

    private static Map<String, Helps> toHelpsMap() {
        return Arrays.stream(Helps.values())
                .distinct()
                .collect(Collectors.toMap(help -> help.toString().toLowerCase().replace("_", "-"), Function.identity()));
    }

    private static Map<String, Operands> toOperandsMap(boolean showOperands, Operands... filters) {
        Collection<Operands> filteredOperands = Arrays.asList(filters);

        return Arrays.stream(Operands.values())
                .distinct()
                .filter(operand -> filteredOperands.isEmpty() || filteredOperands.contains(operand))
                .collect(Collectors.toMap(
                        showOperands ? Operands::getOperand : operands -> operands.name().toLowerCase(),
                        Function.identity()
                ));
    }

    private static Map<String, PerWorldCommands> toPerWorldCommandsMap() {
        return Arrays.stream(PerWorldCommands.values())
                .distinct()
                .collect(Collectors.toMap(PerWorldCommands::getName, Function.identity()));
    }

}
