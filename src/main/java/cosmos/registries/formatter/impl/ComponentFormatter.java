package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import cosmos.registries.formatter.OverflowFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

@Singleton
public class ComponentFormatter implements OverflowFormatter<Component> {

    private final StringFormatter stringFormatter;

    @Inject
    public ComponentFormatter(final Injector injector) {
        this.stringFormatter = injector.getInstance(StringFormatter.class);
    }

    @Override
    public TextComponent asText(final Component value, final boolean keepOverflow) {
        if (keepOverflow) {
            return Component.text().append(value).build();
        }

        return this.stringFormatter.asText(PlainComponentSerializer.plain().serialize(value), keepOverflow).mergeStyle(value); // todo merge style does not work on childs
    }

}
