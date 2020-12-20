package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.Locale;

@Singleton
public class DefaultFormatter implements Formatter<Object> {

    @Override
    public TextComponent asText(Object value) {
        return Component.text(value.toString().toLowerCase(Locale.ROOT));
    }

}
