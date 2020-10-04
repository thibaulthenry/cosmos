package cosmos.commands.weather;

import cosmos.commands.properties.AbstractPropertyCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import cosmos.statics.handlers.TimeConverter;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

public class Thunder extends AbstractPropertyCommand {

    public Thunder() {
        super(
                GenericArguments.optional(
                        GenericArguments.integer(ArgKeys.DURATION_SECONDS.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        int duration = args.<Integer>getOne(ArgKeys.DURATION_SECONDS.t).orElse(0);

        if (duration > 0) {
            worldProperties.setRainTime(TimeConverter.fromSecondsToTicks(duration));
            worldProperties.setThunderTime(TimeConverter.fromSecondsToTicks(duration));
        }

        worldProperties.setRaining(true);
        worldProperties.setThundering(true);
        FinderWorldProperties.saveProperties(worldProperties);

        src.sendMessage(
                Outputs.WEATHER_CHANGE.asText(
                        "stormy",
                        worldProperties,
                        duration > 0 ? Outputs.DURATION_TIME.asText(duration) : Text.EMPTY
                )
        );
    }
}
