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
import org.spongepowered.api.world.border.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.math.vector.Vector2d;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class Information extends AbstractBorderCommand {

    @Override
    protected List<String> additionalAliases() {
        return Collections.singletonList("info");
    }

    private TextComponent getBorderInformation(final Audience src, final ServerWorldProperties properties, final WorldBorder border) {
        final ResourceKey worldKey = properties.key();
        final Vector2d center = border.center();

        final double y = Sponge.server().worldManager().world(worldKey)
                .map(world -> world.highestPositionAt(center.toVector3().toInt()).y())
                .orElse(1);

        final String command = MessageFormat.format("/cm move {0} {1} {2} {3} --safe-only", worldKey.asString(), center.x(), y, center.y());

        final long secondsRemaining = border.timeUntilTargetDiameter().getSeconds();

        final TextComponent contractingTextDetails = secondsRemaining > 0
                ? super.serviceProvider.message()
                .getMessage(src, "success.border.information.contracting.details")
                // todo .replace("diameter", border.newDiameter())
                .replace("time", secondsRemaining)
                .gray()
                .asText() :
                Component.empty();

        return super.serviceProvider.message()
                .getMessage(src, "success.border.information")
                // todo .replace("amount", border.damageAmount())
                .replace("center", center)
                .replace("contracting", secondsRemaining > 0
                        ? Component.text("✓", NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true) :
                        Component.text("✗", NamedTextColor.RED).decoration(TextDecoration.BOLD, true)
                )
                .replace("details", contractingTextDetails)
                .replace("diameter", border.diameter())
                .replace("distance", border.warningDistance())
                // todo .replace("threshold", border.damageThreshold())
                .replace("time", border.warningTime().getSeconds())
                .replace("world", properties)
                .clickEvent("center", ClickEvent.suggestCommand(command))
                .hoverEvent("center", HoverEvent.showText(super.serviceProvider.message().getText(src, "success.border.information.center.hover")))
                .gray()
                .asText();
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        src.sendMessage(CompletableFuture.supplyAsync(() -> getBorderInformation(src, properties, border)).join());
    }

}
