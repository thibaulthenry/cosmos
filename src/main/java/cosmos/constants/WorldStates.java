package cosmos.constants;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public enum WorldStates {
    EXPORTED(TextColors.RED, "Exported"),
    DISABLED(TextColors.GOLD, "Disabled"),
    UNLOADED(TextColors.YELLOW, "Unloaded"),
    LOADED(TextColors.GREEN, "Loaded");

    private final TextColor color;
    private final String name;

    WorldStates(TextColor color, String name) {
        this.color = color;
        this.name = name;
    }

    public TextColor getColor() {
        return color;
    }

    public Text toText() {
        return Text.of(color, name);
    }
}
