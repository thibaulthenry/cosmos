package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.models.backup.BackupArchetype;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

@Singleton
public class BackupArchetypeFormatter implements Formatter<BackupArchetype> {

    @Override
    public TextComponent asText(final BackupArchetype value) {
        return Component.text(value.getName());
    }

}
