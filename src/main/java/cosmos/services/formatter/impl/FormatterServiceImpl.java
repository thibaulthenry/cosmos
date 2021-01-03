package cosmos.services.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import cosmos.registries.formatter.FormatterRegistry;
import cosmos.services.formatter.FormatterService;
import net.kyori.adventure.text.TextComponent;

@Singleton
public class FormatterServiceImpl implements FormatterService {

    private final FormatterRegistry formatterRegistry;

    @Inject
    public FormatterServiceImpl(final FormatterRegistry formatterRegistry) {
        this.formatterRegistry = formatterRegistry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TextComponent asText(final T value) {
        if (this.formatterRegistry.has(value.getClass())) {
            return ((Formatter<T>) this.formatterRegistry.get(value.getClass())).asText(value);
        }

        return this.formatterRegistry.getSuper((Class<T>) value.getClass())
                .map(superFormatter -> superFormatter.asText(value))
                .orElse(this.formatterRegistry.getDefaultFormatter().asText(value));
    }
}
