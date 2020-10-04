package cosmos.commands.time;

import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

public class Dusk extends AbstractTimeChangeCommand {

    @Override
    long getDesiredTime(CommandContext args, WorldProperties worldProperties) {
        return getNearestDusk(worldProperties.getWorldTime());
    }
}
