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

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(key:(\\w+))(,value:(\\w+))?(,color:(\\w+))?(,style:(\\w+))?}}");

    private final String raw;
    private final Collection<TemplateElement> elements;

    Template(final String rawTemplate) {
        this.raw = rawTemplate;
        this.elements = parse(rawTemplate);
    }

    public TextComponent toText(final Map<String, Component> replacements, final Map<String, HoverEvent<?>> hoverEventMap, final Map<String, ClickEvent> clickEventMap) {
        final TextComponent.Builder builder = Component.text();

        elements.forEach(element -> {
            if (element instanceof Placeholder) {
                final Placeholder placeholder = (Placeholder) element;
                final String key = placeholder.key;

                if (replacements.containsKey(key)) {
                    builder.append(placeholder.toText(replacements.get(key), hoverEventMap.get(key), clickEventMap.get(key)));
                    return;
                }

                if (hoverEventMap.containsKey(key) || clickEventMap.containsKey(key)) {
                    builder.append(placeholder.toText(hoverEventMap.get(key), clickEventMap.get(key)));
                    return;
                }
            }

            builder.append(element.toText());
        });

        return builder.build();
    }

    private Collection<TemplateElement> parse(final String rawTemplate) {
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(rawTemplate);

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
                            parseColor(matcher.group(6)).orElse(NamedTextColor.WHITE),
                            parseDecoration(matcher.group(8))
                    )
            );
        }

        flatValue = rawTemplate.substring(flatStart);

        if (!flatValue.isEmpty()) {
            elements.add(new Flat(flatValue));
        }

        return ImmutableList.copyOf(elements.isEmpty() ? Collections.singletonList(new Flat(rawTemplate)) : elements);
    }

    private Optional<TextColor> parseColor(final String color) {
        return Optional.ofNullable(color == null ? null : NamedTextColor.NAMES.value(color));
    }

    private TextDecoration[] parseDecoration(final String decoration) {
        return decoration == null ? new TextDecoration[0] : new TextDecoration[]{TextDecoration.NAMES.value(decoration)};
    }

    private interface TemplateElement {
        TextComponent toText();
    }

    private class Placeholder implements TemplateElement {

        private final String key;
        private final String value;
        private final TextColor color;
        private final TextDecoration[] decorations;

        private Placeholder(final String key, @Nullable final String value, @Nullable final TextColor color, @Nullable final TextDecoration... decorations) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.decorations = decorations == null ? new TextDecoration[0] : decorations;
        }

        @Override
        public TextComponent toText() {
            return Component.text()
                    .content(this.value == null ? this.key : this.value)
                    .color(this.color)
                    .decorate(this.decorations)
                    .build();
        }

        public TextComponent toText(Component replacement) {
            return (value == null ? Component.text().append(replacement).build() : Component.text(this.value))
                    .toBuilder()
                    .color(this.color)
                    .decorate(this.decorations)
                    .build();
        }

        public TextComponent toText(@Nullable final HoverEvent<?> hoverEvent, @Nullable final ClickEvent clickEvent) {
            return this.toText().hoverEvent(hoverEvent).clickEvent(clickEvent);
        }

        public TextComponent toText(final Component replacement, @Nullable final HoverEvent<?> hoverEvent, @Nullable final ClickEvent clickEvent) {
            return this.toText(replacement).hoverEvent(hoverEvent).clickEvent(clickEvent);
        }
    }

    private class Flat implements TemplateElement {

        private final String value;

        private Flat(final String value) {
            this.value = value;
        }

        @Override
        public TextComponent toText() {
            return Component.text(value);
        }
    }

}
