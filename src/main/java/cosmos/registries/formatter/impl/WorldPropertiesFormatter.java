package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.world.server.ServerWorldProperties;
import org.spongepowered.api.world.storage.WorldProperties;

@Singleton
public class WorldPropertiesFormatter implements Formatter<ServerWorldProperties> {

    @Override
    public TextComponent asText(ServerWorldProperties value) {
        return Component.text(value.getKey().asString());
    }

}
