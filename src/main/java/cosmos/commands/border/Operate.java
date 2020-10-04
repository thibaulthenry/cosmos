package cosmos.commands.border;


import cosmos.constants.ArgKeys;
import cosmos.constants.Operands;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

public class Operate extends AbstractBorderCommand {

    public Operate() {
        super(
                Arguments.operandChoices(
                        ArgKeys.OPERAND,
                        true,
                        Operands.PLUS, Operands.MINUS, Operands.TIMES, Operands.DIVIDE
                ),
                GenericArguments.doubleNum(ArgKeys.VALUE.t),
                GenericArguments.optional(
                        GenericArguments.longNum(ArgKeys.DURATION_MILLISECONDS.t)
                )
        );
    }

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        Operands operand = args.<Operands>getOne(ArgKeys.OPERAND.t).orElseThrow(Outputs.INVALID_OPERAND_CHOICE.asSupplier());
        double value = args.<Double>getOne(ArgKeys.VALUE.t).orElseThrow(Outputs.INVALID_VALUE.asSupplier());
        long duration = args.<Long>getOne(ArgKeys.DURATION_MILLISECONDS.t).orElse(0L);

        double startDiameter = worldBorder.getDiameter();
        double endDiameter = startDiameter;

        switch (operand) {
            case PLUS:
                endDiameter = startDiameter + value;
                break;
            case MINUS:
                endDiameter = startDiameter - value;
                break;
            case TIMES:
                endDiameter = startDiameter * value;
                break;
            case DIVIDE:
                endDiameter = startDiameter / value;
                break;
        }

        if (Double.isNaN(endDiameter) || Double.isInfinite(endDiameter)) {
            throw Outputs.NOT_A_NUMBER.asException("Operation result");
        }

        worldBorder.setDiameter(endDiameter, duration);

        worldProperties.setWorldBorderDiameter(startDiameter);
        worldProperties.setWorldBorderTargetDiameter(endDiameter);
        worldProperties.setWorldBorderTimeRemaining(duration);
        FinderWorldProperties.saveProperties(worldProperties);

        src.sendMessage(
                Outputs.BORDER_OPERATE.asText(
                        worldProperties,
                        startDiameter,
                        endDiameter,
                        duration > 0 ? Outputs.DURATION_TIME.asText(duration / 1000) : Text.EMPTY
                )
        );
    }
}
