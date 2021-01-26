package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.math.vector.Vector3d;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class Information extends AbstractBorderCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        src.sendMessage(CompletableFuture.supplyAsync(() -> getBorderInformation(src, properties, border)).join());
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("info");
    }

    private TextComponent getBorderInformation(final Audience src, final ServerWorldProperties properties, final WorldBorder border) {
        final ResourceKey worldKey = properties.getKey();
        final Vector3d center = border.getCenter();

        final double y = Sponge.getServer().getWorldManager().world(worldKey)
                .map(world -> world.getHighestPositionAt(center.toInt()).getY())
                .orElse(1);

        final String command = MessageFormat.format("/cm move {0} {1} {2} {3} --safe-only", worldKey.asString(), center.getX(), y, center.getZ());

        final long secondsRemaining = border.getTimeRemaining().getSeconds();

        final TextComponent contractingTextDetails = secondsRemaining > 0
                ? super.serviceProvider.message()
                .getMessage(src, "success.border.information.contracting.details")
                .replace("diameter", border.getNewDiameter())
                .replace("time", secondsRemaining)
                .gray()
                .asText() :
                Component.empty();

        return super.serviceProvider.message()
                .getMessage(src, "success.border.information")
                .replace("amount", border.getDamageAmount())
                .replace("center", center.toVector2(true))
                .replace("contracting", secondsRemaining > 0
                        ? Component.text("✓", NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true) :
                        Component.text("✗", NamedTextColor.RED).decoration(TextDecoration.BOLD, true)
                )
                .replace("details", contractingTextDetails)
                .replace("diameter", border.getDiameter())
                .replace("distance", border.getWarningDistance())
                .replace("threshold", border.getDamageThreshold())
                .replace("time", border.getWarningTime().getSeconds())
                .replace("world", properties)
                .clickEvent("center", ClickEvent.suggestCommand(command))
                .hoverEvent("center", HoverEvent.showText(super.serviceProvider.message().getText(src, "success.border.information.center.hover")))
                .gray()
                .asText();
    }

}
