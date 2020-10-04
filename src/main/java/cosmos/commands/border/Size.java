package cosmos.commands.border;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class Size extends AbstractBorderCommand {

    public Size() {
        super(
                GenericArguments.optional(
                        GenericArguments.doubleNum(ArgKeys.DIAMETER.t)
                )
        );
    }

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        Optional<Double> optionalDiameter = args.getOne(ArgKeys.DIAMETER.t);
        double diameter = worldBorder.getDiameter();
        String mutableText = "currently";

        if (optionalDiameter.isPresent()) {
            diameter = optionalDiameter.get();
            mutableText = "successfully";
            worldBorder.setDiameter(diameter);
            worldProperties.setWorldBorderDiameter(diameter);
            worldProperties.setWorldBorderTargetDiameter(diameter);
            worldProperties.setWorldBorderTimeRemaining(0);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.BORDER_SIZE.asText(worldProperties, mutableText, diameter));
    }
}
