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

public class WarningTime extends AbstractBorderCommand {

    public WarningTime() {
        super(
                GenericArguments.optional(
                        GenericArguments.integer(ArgKeys.DURATION_SECONDS.t)
                )
        );
    }

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        Optional<Integer> optionalDuration = args.getOne(ArgKeys.DURATION_SECONDS.t);
        int duration = worldBorder.getWarningTime();
        String mutableText = "currently";

        if (optionalDuration.isPresent()) {
            duration = optionalDuration.get();
            mutableText = "successfully";
            worldBorder.setWarningTime(duration);
            worldProperties.setWorldBorderWarningTime(duration);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.BORDER_WARNING_TIME.asText(worldProperties, mutableText, duration));
    }
}
