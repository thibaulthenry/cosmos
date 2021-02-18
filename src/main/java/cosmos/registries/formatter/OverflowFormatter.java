package cosmos.registries.formatter;

import net.kyori.adventure.text.TextComponent;

public interface OverflowFormatter<T> extends Formatter<T> {

    default TextComponent asText(final T value) {
        return this.asText(value, false);
    }

    TextComponent asText(T value, boolean keepOverflow);

}
