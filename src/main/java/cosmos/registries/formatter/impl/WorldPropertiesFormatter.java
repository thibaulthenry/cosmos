package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.world.storage.WorldProperties;

@Singleton
public class WorldPropertiesFormatter implements Formatter<WorldProperties> {

    @Override
    public TextComponent asText(WorldProperties value) {
        return Component.text(value.getKey().asString());
    }

}
