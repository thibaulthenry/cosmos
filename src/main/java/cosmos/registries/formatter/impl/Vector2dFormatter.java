package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.math.GenericMath;
import org.spongepowered.math.vector.Vector2d;

@Singleton
public class Vector2dFormatter implements Formatter<Vector2d> {

    @Override
    public TextComponent asText(final Vector2d value) {
        final double x = GenericMath.round(value.getX(), 2);
        final double z = GenericMath.round(value.getY(), 2);

        final TextComponent hoverText = Component.text()
                .append(Component.text("<x: "))
                .append(Component.text(x, NamedTextColor.GOLD))
                .append(Component.text(", z: "))
                .append(Component.text(z, NamedTextColor.GOLD))
                .append(Component.text(">"))
                .color(NamedTextColor.WHITE)
                .build();

        return Component.text()
                .append(Component.text("("))
                .append(Component.text((int) x, NamedTextColor.GOLD))
                .append(Component.text(", "))
                .append(Component.text((int) z, NamedTextColor.GOLD))
                .append(Component.text(")"))
                .hoverEvent(HoverEvent.showText(hoverText))
                .color(NamedTextColor.WHITE)
                .build();
    }

}
