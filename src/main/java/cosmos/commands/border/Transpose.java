package cosmos.commands.border;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

public class Transpose extends AbstractBorderCommand {

    public Transpose() {
        super(
                GenericArguments.longNum(ArgKeys.DURATION_MILLISECONDS.t),
                GenericArguments.doubleNum(ArgKeys.END_DIAMETER.t),
                GenericArguments.optional(
                        GenericArguments.doubleNum(ArgKeys.START_DIAMETER.t)
                )
        );
    }


    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        double endDiameter = args.<Double>getOne(ArgKeys.END_DIAMETER.t)
                .orElseThrow(Outputs.INVALID_BLOCKS_DISTANCE.asSupplier());
        double startDiameter = args.<Double>getOne(ArgKeys.START_DIAMETER.t).orElse(worldBorder.getDiameter());
        long duration = args.<Long>getOne(ArgKeys.DURATION_MILLISECONDS.t)
                .orElseThrow(Outputs.INVALID_DURATION.asSupplier());

        worldBorder.setDiameter(startDiameter, endDiameter, duration);

        worldProperties.setWorldBorderDiameter(startDiameter);
        worldProperties.setWorldBorderTargetDiameter(endDiameter);
        worldProperties.setWorldBorderTimeRemaining(duration);
        FinderWorldProperties.saveProperties(worldProperties);

        src.sendMessage(
                Outputs.BORDER_TRANSPOSE.asText(
                        worldProperties,
                        startDiameter,
                        endDiameter,
                        duration > 0 ? Outputs.DURATION_TIME.asText(duration / 1000) : Text.EMPTY
                )
        );
    }
}
