package cosmos.registries.template;

import com.google.common.collect.ImmutableList;
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
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(key:(\\w+))(;value:([^;}]+))?(;valueElse:([^;}]+))?(;color:(\\w+))?(;style:(\\w+))?}}");

    private final Collection<TemplateElement> elements;

    Template(final String rawTemplate) {
        this.elements = parse(rawTemplate);
    }

    public TextComponent toText(final Map<String, Component> replacements, final Map<String, Boolean> conditionMap, final Map<String, HoverEvent<?>> hoverEventMap, final Map<String, ClickEvent> clickEventMap, final TextColor defaultColor) {
        final TextComponent.Builder builder = Component.text();

        for (TemplateElement element : this.elements) {
            if (element instanceof Placeholder) {
                final Placeholder placeholder = (Placeholder) element;
                final String key = placeholder.key;

                if (!conditionMap.getOrDefault(key, true)) {
                    if (placeholder.valueElse != null) {
                        builder.append(placeholder.toText(null, true, defaultColor, hoverEventMap.get(key), clickEventMap.get(key)));
                    }
                } else {
                    builder.append(placeholder.toText(replacements.get(key), false, defaultColor, hoverEventMap.get(key), clickEventMap.get(key)));
                }
            } else {
                builder.append(element.toText());
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
                            parseColor(matcher.group(8)),
                            parseDecoration(matcher.group(10))
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
        if ("default".equals(color)) {
            return null;
        }

        return Optional.ofNullable(color == null ? null : NamedTextColor.NAMES.value(color)).orElse(NamedTextColor.WHITE);
    }

    private TextDecoration[] parseDecoration(final String decoration) {
        return decoration == null ? new TextDecoration[0] : new TextDecoration[]{TextDecoration.NAMES.value(decoration)};
    }

    private interface TemplateElement {
        TextComponent toText();

        String getValue();
    }

    private class Placeholder implements TemplateElement {

        private final String key;
        private final String value;
        private final String valueElse;
        private final TextColor color;
        private final TextDecoration[] decorations;

        private Placeholder(final String key, @Nullable final String value, @Nullable final String valueElse, @Nullable final TextColor color, @Nullable final TextDecoration... decorations) {
            this.key = key;
            this.value = value;
            this.valueElse = valueElse;
            this.color = color;
            this.decorations = decorations == null ? new TextDecoration[0] : decorations;
        }

        public TextComponent toText(@Nullable final Component replacement, final boolean useElse, @Nullable final TextColor defaultColor, @Nullable final HoverEvent<?> hoverEvent, @Nullable final ClickEvent clickEvent) {
            final TextComponent.Builder builder = Component.text();

            if (useElse && this.valueElse != null) {
                builder.content(this.valueElse);
            } else if (this.value != null) {
                builder.content(this.value);
            } else if (replacement != null) {
                builder.append(replacement);
            } else {
                builder.content(this.key);
            }

            return builder
                    .color(this.color == null ? defaultColor : this.color)
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

    private class Flat implements TemplateElement {

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
