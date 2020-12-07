package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.TextComponent;

@Singleton
public class TextComponentFormatter implements Formatter<TextComponent> {

    @Override
    public TextComponent asText(TextComponent value) {
        return value;
    }

}
