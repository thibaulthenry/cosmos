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

public class WarningDistance extends AbstractBorderCommand {

    public WarningDistance() {
        super(
                GenericArguments.optional(
                        GenericArguments.integer(ArgKeys.DISTANCE.t)
                )
        );
    }

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        Optional<Integer> optionalDistance = args.getOne(ArgKeys.DISTANCE.t);
        int distance = worldBorder.getWarningDistance();
        String mutableText = "currently";

        if (optionalDistance.isPresent()) {
            distance = optionalDistance.get();
            mutableText = "successfully";
            worldBorder.setWarningDistance(distance);
            worldProperties.setWorldBorderWarningDistance(distance);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.BORDER_WARNING_DISTANCE.asText(worldProperties, mutableText, distance));
    }
}
