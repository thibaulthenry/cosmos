package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;

@Singleton
public class KeyedFormatter implements Formatter<Keyed> {

    private final KeyFormatter keyFormatter;

    @Inject
    public KeyedFormatter(final Injector injector) {
        this.keyFormatter = injector.getInstance(KeyFormatter.class);
    }

    @Override
    public TextComponent asText(final Keyed value) {
        return this.keyFormatter.asText(value.key());
    }

}