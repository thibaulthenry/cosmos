package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.services.ServiceProvider;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class PlayerNames implements ValueParameter<List<Component>> {

    private final ServiceProvider serviceProvider;
    private final ResourceKeyedValueParameter<List<Entity>> manyEntities = ResourceKeyedValueParameters.MANY_ENTITIES.get();

    @Inject
    public PlayerNames(final ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        this.manyEntities.complete(context, currentInput);

        return this.getWorldPlayerNames(context)
                .stream()
                .filter(name -> name.startsWith(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends List<Component>> getValue(Parameter.Key<? super List<Component>> parameterKey, ArgumentReader.Mutable reader, CommandContext.Builder context) throws ArgumentParseException {
        final String input = reader.peekString();

        if ("*".equals(input) || input.startsWith("\"{") || input.startsWith("{")) {
            return Optional.empty();
        }

        return this.manyEntities.getValue(Parameter.key("player", new TypeToken<List<Entity>>() {}), reader, context)
                .map(entities -> entities
                        .stream()
                        .map(entity -> (entity instanceof Player) ? ((Tamer) entity).getName() : entity.getUniqueId().toString())
                        .map(Component::text)
                        .collect(Collectors.toList())
                );
    }

    private Collection<String> getWorldPlayerNames(final CommandContext context) {
        return this.serviceProvider.world().findKeyOrSource(context)
                .flatMap(worldKey -> Sponge.getServer().getWorldManager().world(worldKey))
                .map(world -> world.getPlayers().stream().map(ServerPlayer::getName).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

}
