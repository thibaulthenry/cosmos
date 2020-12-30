package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

@Singleton
public class StringFormatter implements Formatter<String> {

    private static final int MAX_TEXT_LENGTH = 24;

    @Override
    public TextComponent asText(final String value) {
        return Component.text(this.substring(value));
    }

    private String substring(final String value) {
        return (value.length() > MAX_TEXT_LENGTH) ? value.substring(0, MAX_TEXT_LENGTH) + ".." : value;
    }

}
