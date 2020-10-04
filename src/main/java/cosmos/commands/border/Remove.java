package cosmos.commands.border;

import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

public class Remove extends AbstractBorderCommand {

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        worldBorder.setDiameter(Double.POSITIVE_INFINITY);

        worldProperties.setWorldBorderDiameter(Double.POSITIVE_INFINITY);
        worldProperties.setWorldBorderTargetDiameter(Double.POSITIVE_INFINITY);
        worldProperties.setWorldBorderTimeRemaining(0);
        FinderWorldProperties.saveProperties(worldProperties);

        src.sendMessage(Outputs.BORDER_REMOVE.asText(worldProperties));
    }
}
