package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

@Singleton
public class ServerWorldFormatter implements Formatter<ServerWorld> {

    private final KeyFormatter keyFormatter;

    @Inject
    public ServerWorldFormatter(final Injector injector) {
        this.keyFormatter = injector.getInstance(KeyFormatter.class);
    }

    @Override
    public TextComponent asText(final ServerWorld value) {
        return this.keyFormatter.asText(value.getKey());
    }

}