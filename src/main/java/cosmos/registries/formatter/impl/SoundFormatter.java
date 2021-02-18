package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.services.message.MessageService;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.util.Ticks;

import java.util.Locale;

@Singleton
public class SoundFormatter implements LocaleFormatter<Sound> {

    private final KeyFormatter keyFormatter;
    private final MessageService messageService;

    @Inject
    public SoundFormatter(final Injector injector) {
        this.keyFormatter = injector.getInstance(KeyFormatter.class);
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public TextComponent asText(final Sound value, final Locale locale) {
        return this.keyFormatter.asText(value.name()).hoverEvent(HoverEvent.showText(this.asTextHover(value, locale)));
    }

    public TextComponent asText(final Sound value, @Nullable final Ticks interval, final Locale locale) {
        return this.keyFormatter.asText(value.name()).hoverEvent(HoverEvent.showText(this.asTextHover(value, interval, locale)));
    }

    public TextComponent asTextHover(final Sound value, final Locale locale) {
        return this.asTextHover(value, null, locale);
    }

    public TextComponent asTextHover(final Sound value, @Nullable final Ticks interval, final Locale locale) {
        return this.messageService.getMessage(locale, "formatter.cosmos-portal.hover.sound")
                .replace("sound_interval", interval == null ? null : interval.ticks() * 50)
                .replace("sound_pitch", value.pitch())
                .replace("sound_type", value.name())
                .replace("sound_volume", value.volume())
                .condition("sound_interval", interval != null)
                .condition("sound_pitch", true)
                .condition("sound_type", true)
                .condition("sound_volume", true)
                .gray()
                .asText();
    }

}
