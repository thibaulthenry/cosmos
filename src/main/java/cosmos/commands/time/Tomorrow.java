package cosmos.commands.time;

import cosmos.constants.Units;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

public class Tomorrow extends AbstractTimeChangeCommand {

    @Override
    long getDesiredTime(CommandContext args, WorldProperties worldProperties) {
        long worldTime = worldProperties.getWorldTime();
        return worldTime + getFutureTickDifference(worldProperties.getWorldTime(), Units.DAWN, true);
    }
}