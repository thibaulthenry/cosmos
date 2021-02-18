package cosmos.constants;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum WorldStates {

    EXPORTED(NamedTextColor.RED),
    UNLOADED(NamedTextColor.YELLOW),
    LOADED(NamedTextColor.GREEN);

    private final TextColor color;

    WorldStates(final TextColor color) {
        this.color = color;
    }

    public TextColor color() {
        return this.color;
    }

}
