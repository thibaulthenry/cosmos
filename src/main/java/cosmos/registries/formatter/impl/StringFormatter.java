package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

@Singleton
public class StringFormatter implements Formatter<String> {

    private static final int MAX_TEXT_LENGTH = 24;

    @Override
    public TextComponent asText(String value) {
        return Component.text(this.substring(value, MAX_TEXT_LENGTH));
    }

    private String substring(String value, int maxLength) {
        return (value.length() > maxLength) ? value.substring(0, maxLength) + ".." : value;
    }

}
