package cosmos.models.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum WorldStates {
    EXPORTED(NamedTextColor.RED, "Exported"),
    DISABLED(NamedTextColor.GOLD, "Disabled"),
    UNLOADED(NamedTextColor.YELLOW, "Unloaded"),
    LOADED(NamedTextColor.GREEN, "Loaded");

    private final TextColor color;
    private final String name;

    WorldStates(final TextColor color, final String name) {
        this.color = color;
        this.name = name;
    }

    public TextColor getColor() {
        return this.color;
    }

    public TextComponent toText() {
        return Component.text(this.name, this.color);
    }
}
