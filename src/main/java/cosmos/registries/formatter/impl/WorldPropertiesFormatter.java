package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.world.server.ServerWorldProperties;

@Singleton
public class WorldPropertiesFormatter implements Formatter<ServerWorldProperties> {

    @Override
    public TextComponent asText(final ServerWorldProperties value) {
        return Component.text(value.getKey().asString());
    }

}
