package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;

@Singleton
public class KeyFormatter implements Formatter<Key> {

    @Override
    public TextComponent asText(final Key value) {
        return Component.text()
                .append(Component.text(value.value()))
                .hoverEvent(HoverEvent.showText(Component.text(value.asString())))
                .build();
    }

}