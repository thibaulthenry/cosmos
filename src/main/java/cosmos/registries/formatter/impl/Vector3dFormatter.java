package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.math.GenericMath;
import org.spongepowered.math.vector.Vector3d;

@Singleton
public class Vector3dFormatter implements Formatter<Vector3d> {

    @Override
    public TextComponent asText(final Vector3d value) {
        final double x = GenericMath.round(value.x(), 2);
        final double y = GenericMath.round(value.y(), 2);
        final double z = GenericMath.round(value.z(), 2);

        final TextComponent hoverText = Component.text()
                .append(Component.text("<x: "))
                .append(Component.text(x, NamedTextColor.GOLD))
                .append(Component.text(", y: "))
                .append(Component.text(y, NamedTextColor.GOLD))
                .append(Component.text(", z: "))
                .append(Component.text(z, NamedTextColor.GOLD))
                .append(Component.text(">"))
                .color(NamedTextColor.WHITE)
                .build();

        return Component.text()
                .append(Component.text("("))
                .append(Component.text((int) x, NamedTextColor.GOLD))
                .append(Component.text(", "))
                .append(Component.text((int) y, NamedTextColor.GOLD))
                .append(Component.text(", "))
                .append(Component.text((int) z, NamedTextColor.GOLD))
                .append(Component.text(")"))
                .hoverEvent(HoverEvent.showText(hoverText))
                .color(NamedTextColor.WHITE)
                .build();
    }

}
