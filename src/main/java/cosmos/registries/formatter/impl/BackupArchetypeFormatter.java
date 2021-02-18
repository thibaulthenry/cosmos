package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.backup.BackupArchetype;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.services.message.MessageService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Singleton
public class BackupArchetypeFormatter implements LocaleFormatter<BackupArchetype> {

    private final MessageService messageService;

    @Inject
    public BackupArchetypeFormatter(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public TextComponent asText(final BackupArchetype backupArchetype, final Locale locale) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
        final boolean hasTag = backupArchetype.tag().isPresent();

        final TextComponent hoverText = this.messageService.getMessage(locale, "formatter.backup-archetype.hover")
                .replace("date", dateTimeFormatter.format(backupArchetype.creationDateTime()))
                .replace("name", backupArchetype.name())
                .replace("tag2", backupArchetype.tag().orElse(""))
                .replace("world", backupArchetype.worldKey().formatted())
                .condition("tag1", hasTag)
                .condition("tag2", hasTag)
                .gray()
                .asText();

        return Component.text(backupArchetype.name()).hoverEvent(HoverEvent.showText(hoverText));
    }

}
