package cosmos.registries.template;

import com.google.common.collect.ImmutableList;
import cosmos.Cosmos;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(key:(\\w+))(;value:([^;}]+))?(;valueElse:([^;}]+))?(;prefix:([^;}]+))?(;suffix:([^;}]+))?(;overflow:keep)?(;color:(\\w+))?(;colorElse:(\\w+))?(;style:(\\w+))?}}");

    private final Collection<TemplateElement> elements;
    private final Locale locale;

    Template(final Locale locale, final String rawTemplate) {
        this.elements = parse(rawTemplate);
        this.locale = locale;
    }

    public TextComponent toText(final Map<String, Object> replacements, final Map<String, Boolean> conditionMap, final Map<String, HoverEvent<?>> hoverEventMap, final Map<String, ClickEvent> clickEventMap, final TextColor defaultColor) {
        final TextComponent.Builder builder = Component.text();

        for (TemplateElement element : this.elements) {
            if (!(element instanceof Placeholder)) {
                builder.append(element.toText());
                continue;
            }

            final Placeholder placeholder = (Placeholder) element;
            final String key = placeholder.key;

            if (conditionMap.getOrDefault(key, true)) {
                TextComponent formattedReplacement = null;

                if (replacements.containsKey(key)) {
                    formattedReplacement = Cosmos.getServices().format().asText(replacements.get(key), this.locale, placeholder.keepOverflow);
                }

                builder.append(placeholder.toText(formattedReplacement, false, defaultColor, hoverEventMap.get(key), clickEventMap.get(key)));
            } else if (placeholder.valueElse != null) {
                builder.append(placeholder.toText(null, true, defaultColor, hoverEventMap.get(key), clickEventMap.get(key)));
            }
        }

        return builder.build();
    }

    private Collection<TemplateElement> parse(final String rawTemplate) {
        final Matcher matcher = Template.PLACEHOLDER_PATTERN.matcher(rawTemplate);

        final List<TemplateElement> elements = new ArrayList<>();

        int flatStart = 0;
        String flatValue;

        while (matcher.find()) {
            flatValue = rawTemplate.substring(flatStart, matcher.start());
            flatStart = matcher.end();

            if (!flatValue.isEmpty()) {
                elements.add(new Flat(flatValue));
            }

            elements.add(
                    new Placeholder(
                            matcher.group(2),
                            matcher.group(4),
                            matcher.group(6),
                            matcher.group(8),
                            matcher.group(10),
                            matcher.group(11) != null,
                            parseColor(matcher.group(13)),
                            parseColor(matcher.group(15)),
                            parseDecoration(matcher.group(17))
                    )
            );
        }

        flatValue = rawTemplate.substring(flatStart);

        if (!flatValue.isEmpty()) {
            elements.add(new Flat(flatValue));
        }

        return ImmutableList.copyOf(elements.isEmpty() ? Collections.singletonList(new Flat(rawTemplate)) : elements);
    }

    private TextColor parseColor(final String color) {
        return color == null ? null : NamedTextColor.NAMES.value(color);
    }

    private TextDecoration[] parseDecoration(final String decoration) {
        return decoration == null ? new TextDecoration[0] : new TextDecoration[]{TextDecoration.NAMES.value(decoration)};
    }

    private interface TemplateElement {
        TextComponent toText();

        String getValue();
    }

    private static class Placeholder implements TemplateElement {

        private final String key;
        private final String value;
        private final String valueElse;
        private final String prefix;
        private final String suffix;
        private final boolean keepOverflow;
        private final TextColor color;
        private final TextColor colorElse;
        private final TextDecoration[] decorations;

        private Placeholder(final String key, @Nullable final String value, @Nullable final String valueElse,
                            @Nullable final String prefix, @Nullable final String suffix, boolean keepOverflow,
                            @Nullable final TextColor color, @Nullable final TextColor colorElse,
                            @Nullable final TextDecoration... decorations) {
            this.key = key;
            this.value = value;
            this.valueElse = valueElse;
            this.prefix = prefix;
            this.suffix = suffix;
            this.keepOverflow = keepOverflow;
            this.color = color;
            this.colorElse = colorElse;
            this.decorations = decorations == null ? new TextDecoration[0] : decorations;
        }

        public TextComponent toText(@Nullable final Component replacement, final boolean useElse, @Nullable final TextColor defaultColor, @Nullable final HoverEvent<?> hoverEvent, @Nullable final ClickEvent clickEvent) {
            final TextComponent.Builder builder = Component.text();

            if (this.prefix != null) {
                builder.append(Component.text(this.prefix, defaultColor));
            }

            if (useElse && this.valueElse != null) {
                builder.append(Component.text(this.valueElse));
            } else if (this.value != null) {
                builder.append(Component.text(this.value));
            } else if (replacement != null) {
                builder.append(replacement);
            } else {
                builder.append(Component.text(this.key));
            }

            if (this.suffix != null) {
                builder.append(Component.text(this.suffix, defaultColor));
            }

            if (useElse && this.colorElse != null) {
                builder.color(this.colorElse);
            } else if (this.color != null) {
                builder.color(this.color);
            } else {
                builder.color(defaultColor);
            }

            return builder
                    .decorate(this.decorations)
                    .hoverEvent(hoverEvent)
                    .clickEvent(clickEvent)
                    .build();
        }

        @Override
        public TextComponent toText() {
            return this.toText(null, false, null, null, null);
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    private static class Flat implements TemplateElement {

        private final String value;

        private Flat(final String value) {
            this.value = value;
        }

        @Override
        public TextComponent toText() {
            return Component.text(this.value);
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

}
