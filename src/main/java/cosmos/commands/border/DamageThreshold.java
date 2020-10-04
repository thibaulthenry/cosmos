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

public class DamageThreshold extends AbstractBorderCommand {

    public DamageThreshold() {
        super(
                GenericArguments.optional(
                        GenericArguments.doubleNum(ArgKeys.DISTANCE.t)
                )
        );
    }

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        Optional<Double> optionalDistance = args.getOne(ArgKeys.DISTANCE.t);
        double distance = worldBorder.getDamageThreshold();
        String mutableText = "currently";

        if (optionalDistance.isPresent()) {
            distance = optionalDistance.get();
            mutableText = "successfully";
            worldBorder.setDamageThreshold(distance);
            worldProperties.setWorldBorderDamageThreshold(distance);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.BORDER_DAMAGE_THRESHOLD.asText(worldProperties, mutableText, distance));
    }
}
