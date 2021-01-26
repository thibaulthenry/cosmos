package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.services.message.MessageService;
import cosmos.services.template.TemplateService;
import cosmos.services.transportation.TransportationService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.GenericMath;
import org.spongepowered.math.vector.Vector3d;

import java.util.Locale;

@Singleton
public class ServerLocationFormatter implements LocaleFormatter<ServerLocation> {

    private final MessageService messageService;
    private final TransportationService transportationService;

    @Inject
    public ServerLocationFormatter(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
        this.transportationService = injector.getInstance(TransportationService.class);
    }

    @Override
    public TextComponent asText(final ServerLocation value, final Locale locale) {
        final ResourceKey worldKey = value.getWorldKey();
        final double x = GenericMath.round(value.getX(), 2);
        final double y = GenericMath.round(value.getY(), 2);
        final double z = GenericMath.round(value.getZ(), 2);
        final Vector3d position = Vector3d.from(x, y, z);

        final TextComponent hoverText = this.messageService.getMessage(locale, "formatter.server-location.hover")
                .replace("position", position)
                .replace("world", worldKey)
                .asText();

        final String command = this.transportationService.buildCommand(null, worldKey, position, null, false);

        return Component.text()
                .append(Component.text("("))
                .append(Component.text((int) x, NamedTextColor.GOLD))
                .append(Component.text(", "))
                .append(Component.text((int) y, NamedTextColor.GOLD))
                .append(Component.text(", "))
                .append(Component.text((int) z, NamedTextColor.GOLD))
                .append(Component.text(")"))
                .hoverEvent(HoverEvent.showText(hoverText))
                .clickEvent(ClickEvent.suggestCommand(command))
                .build();
    }

}
