package cosmos.commands.weather;

import cosmos.commands.properties.AbstractPropertyCommand;
import cosmos.constants.Outputs;
import cosmos.statics.handlers.TimeConverter;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

public class Forecast extends AbstractPropertyCommand {

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) {
        String weather = "stormy";
        int duration = Math.min(worldProperties.getRainTime(), worldProperties.getThunderTime());

        if (!worldProperties.isThundering()) {
            weather = "rainy";
            duration = worldProperties.getRainTime();
        }

        if (!worldProperties.isRaining()) {
            weather = "sunny";
        }

        src.sendMessage(
                Outputs.WEATHER_FORECAST.asText(
                        worldProperties,
                        weather,
                        duration > 0 ? Outputs.DURATION_TIME.asText(TimeConverter.fromTicksToSeconds(duration)) : Text.EMPTY
                )
        );
    }
}
