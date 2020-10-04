package cosmos.commands.border;

import com.flowpowered.math.vector.Vector3d;
import cosmos.constants.Outputs;
import cosmos.statics.handlers.OutputFormatter;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Arrays;

public class Information extends AbstractBorderCommand {

    @Override
    @SuppressWarnings("HardcodedFileSeparator")
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        String worldName = worldProperties.getWorldName();

        Vector3d center = worldBorder.getCenter();

        double y = Sponge.getServer().getWorld(worldProperties.getUniqueId())
                .map(world -> world.getHighestPositionAt(center.toInt()).getY())
                .orElse(1);

        String command = StringUtils.joinWith(" ", "/cm move ", worldName, center.getX(), y, center.getZ(), "--safe-only");

        Text centerText = Text.builder()
                .append(Text.of(TextColors.GRAY, "Center position: "))
                .append(Text.builder()
                        .append(Text.of(OutputFormatter.formatOutput(center.toVector2(true))))
                        .onHover(TextActions.showText(Text.of("Click to move to this position (safe-only)")))
                        .onClick(TextActions.suggestCommand(command))
                        .build()
                )
                .build();

        Text diameterText = Text.builder()
                .append(Text.of(TextColors.GRAY, "Diameter: "))
                .append(Text.of(TextColors.GREEN, worldBorder.getDiameter()))
                .build();

        long secondsRemaining = worldBorder.getTimeRemaining() / 1000;
        Text movingText = Text.builder()
                .append(Text.of(TextColors.GRAY, "Moving: "))
                .append(secondsRemaining > 0 ?
                        Text.of(TextColors.GREEN, TextStyles.BOLD, "✓") :
                        Text.of(TextColors.RED, TextStyles.BOLD, "✗"))
                .append(secondsRemaining > 0 ?
                        Text.of(
                                TextColors.WHITE, " <Seconds: ", TextColors.GOLD, secondsRemaining,
                                TextColors.WHITE, ", Target Diameter: ", TextColors.GOLD, worldBorder.getNewDiameter(),
                                TextColors.WHITE, ">") :
                        Text.EMPTY)
                .build();

        Text damageText = Text.builder()
                .append(Text.of(TextColors.GRAY, "Damage: "))
                .append(Text.of(TextColors.WHITE, "<Amount: "))
                .append(Text.of(TextColors.RED, worldBorder.getDamageAmount()))
                .append(Text.of(TextColors.WHITE, ", Threshold: "))
                .append(Text.of(TextColors.RED, worldBorder.getDamageThreshold()))
                .append(Text.of(TextColors.WHITE, ">"))
                .build();

        Text warningText = Text.builder()
                .append(Text.of(TextColors.GRAY, "Warning: "))
                .append(Text.of(TextColors.WHITE, "<Distance: "))
                .append(Text.of(TextColors.GOLD, worldBorder.getWarningDistance()))
                .append(Text.of(TextColors.WHITE, ", Time: "))
                .append(Text.of(TextColors.GOLD, worldBorder.getWarningTime()))
                .append(Text.of(TextColors.WHITE, ">"))
                .build();

        Iterable<Text> contents = Arrays.asList(centerText, diameterText, movingText, damageText, warningText);

        Text title = Outputs.SHOW_BORDER_INFORMATION.asText(worldName);

        sendPaginatedOutput(src, title, contents, false);
    }
}
