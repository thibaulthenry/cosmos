package cosmos.services.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import cosmos.registries.formatter.FormatterRegistry;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.registries.formatter.OverflowFormatter;
import cosmos.services.formatter.FormatterService;
import net.kyori.adventure.text.TextComponent;

import java.util.Locale;
import java.util.Optional;

@Singleton
public class FormatterServiceImpl implements FormatterService {

    private final FormatterRegistry formatterRegistry;

    @Inject
    public FormatterServiceImpl(final FormatterRegistry formatterRegistry) {
        this.formatterRegistry = formatterRegistry;
    }

    @Override
    public <T> TextComponent asText(final T value) {
        return this.asText(value, Locale.ROOT, false);
    }

    @Override
    public <T> TextComponent asText(final T value, final boolean keepOverflow) {
        return this.asText(value, Locale.ROOT, keepOverflow);
    }

    @Override
    public <T> TextComponent asText(final T value, final Locale locale) {
        return this.asText(value, locale, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TextComponent asText(final T value, final Locale locale, boolean keepOverflow) {
        final Optional<Formatter<? super T>> optionalFormatter = this.findFormatter(value);

        if (!optionalFormatter.isPresent()) {
            return this.formatterRegistry.defaultFormatter().asText(value);
        }

        final Formatter<? super T> formatter = optionalFormatter.get();

        if (formatter instanceof LocaleFormatter) {
            return ((LocaleFormatter<T>) formatter).asText(value, locale);
        }

        if (formatter instanceof OverflowFormatter) {
            return ((OverflowFormatter<T>) formatter).asText(value, keepOverflow);
        }

        return formatter.asText(value);
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<Formatter<? super T>> findFormatter(final T value) {
        if (this.formatterRegistry.has(value.getClass())) {
            return Optional.of((Formatter<T>) this.formatterRegistry.value(value.getClass()));
        }

        return this.formatterRegistry.findSuper((Class<T>) value.getClass());
    }

}
