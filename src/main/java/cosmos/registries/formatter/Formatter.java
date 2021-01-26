package cosmos.registries.formatter;

import net.kyori.adventure.text.TextComponent;

public interface Formatter<T> {

    TextComponent asText(T value);

}
