package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.OverflowFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

@Singleton
public class StringFormatter implements OverflowFormatter<String> {

    private static final int MAX_TEXT_LENGTH = 14;

    @Override
    public TextComponent asText(final String value, boolean keepOverflow) {
        if (!keepOverflow && value.length() > StringFormatter.MAX_TEXT_LENGTH) {
            return Component.text(value.substring(0, StringFormatter.MAX_TEXT_LENGTH) + "…")
                    .hoverEvent(HoverEvent.showText(Component.text(value)));
        }

        return Component.text(value);
    }

}
