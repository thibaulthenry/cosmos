package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

@Singleton
public class KeyedFormatter implements Formatter<Keyed> {

    @Override
    public TextComponent asText(final Keyed value) {
        return Component.text(value.key().asString());
    }

}