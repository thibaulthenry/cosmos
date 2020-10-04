package cosmos.commands.border;

import com.flowpowered.math.vector.Vector2d;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class Center extends AbstractBorderCommand {

    public Center() {
        super(
                GenericArguments.optional(
                        Arguments.vector2d(ArgKeys.POSITION_XZ)
                )
        );
    }

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        Optional<Vector2d> optionalCenter = args.getOne(ArgKeys.POSITION_XZ.t);
        Vector2d center = worldProperties.getWorldBorderCenter().toVector2(true);

        String mutableText = "currently";

        if (optionalCenter.isPresent()) {
            mutableText = "successfully";
            center = optionalCenter.get();
            worldBorder.setCenter(center.getX(), center.getY());
            worldProperties.setWorldBorderCenter(center.getX(), center.getY());
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.BORDER_CENTER.asText(worldProperties, mutableText, center));
    }
}
