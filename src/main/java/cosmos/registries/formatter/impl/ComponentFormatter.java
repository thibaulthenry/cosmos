package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.OverflowFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

@Singleton
public class ComponentFormatter implements OverflowFormatter<Component> {

    private static final int MAX_TEXT_LENGTH = 14;

    @Override
    public TextComponent asText(final Component value, final boolean keepOverflow) {
        if (keepOverflow || PlainTextComponentSerializer.plainText().serialize(value).length() <= MAX_TEXT_LENGTH) {
            return Component.text().append(value).build();
        }

        final Component shortenedText = value.children().isEmpty()
                ? this.toShortenedTextWithoutChildren(value, MAX_TEXT_LENGTH)
                : this.toShortenedTextWithChildren(value, MAX_TEXT_LENGTH);

        return Component.text().append(shortenedText).hoverEvent(HoverEvent.showText(value)).build();
    }

    private Component toShortenedTextWithoutChildren(final Component text, final int remainingSpace) {
        final String plain = PlainTextComponentSerializer.plainText().serialize(text);
        return Component.text((plain.length() >= remainingSpace) ? plain.substring(0, remainingSpace) + "…" : plain).mergeStyle(text);
    }

    private Component toShortenedTextWithChildren(final Component text, int remainingSpace) {
        final TextComponent.Builder builder = Component.text().mergeStyle(text);

        for (final Component childText : text.children()) {
            if (remainingSpace == 0) {
                break;
            }

            if (childText.children().isEmpty()) {
                final Component shortenedText = this.toShortenedTextWithoutChildren(childText, remainingSpace);
                remainingSpace = Math.max(0, remainingSpace - PlainTextComponentSerializer.plainText().serialize(shortenedText).length());
                builder.append(shortenedText);
            } else {
                builder.append(this.toShortenedTextWithChildren(childText, remainingSpace));
            }
        }

        return builder.build();
    }

}
