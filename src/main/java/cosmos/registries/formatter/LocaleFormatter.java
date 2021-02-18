package cosmos.registries.formatter;

import net.kyori.adventure.text.TextComponent;

import java.util.Locale;

public interface LocaleFormatter<T> extends Formatter<T> {

    default TextComponent asText(final T value) {
        return this.asText(value, Locale.ROOT);
    }

    TextComponent asText(T value, Locale locale);

}
