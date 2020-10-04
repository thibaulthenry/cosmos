package cosmos.commands.time;

import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

public class Dawn extends AbstractTimeChangeCommand {

    @Override
    long getDesiredTime(CommandContext args, WorldProperties worldProperties) {
        return getNearestDawn(worldProperties.getWorldTime());
    }
}
