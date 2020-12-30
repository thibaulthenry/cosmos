package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.math.GenericMath;
import org.spongepowered.math.vector.Vector2d;

@Singleton
public class Vector2dFormatter implements Formatter<Vector2d> {

    @Override
    public TextComponent asText(final Vector2d value) {
        return Component.text()
                .append(Component.text("<x: "))
                .append(Component.text(GenericMath.round(value.getX(), 2), NamedTextColor.GOLD))
                .append(Component.text(", z: "))
                .append(Component.text(GenericMath.round(value.getY(), 2), NamedTextColor.GOLD))
                .append(Component.text(">"))
                .build();
    }

}
