package cosmos.registries.formatter;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.backup.BackupArchetype;
import cosmos.registries.formatter.impl.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector2d;
import org.spongepowered.math.vector.Vector3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class FormatterRegistry implements CosmosRegistry<Class<?>, Formatter<?>> {

    private final Map<Class<?>, Formatter<?>> formatterMap = new HashMap<>();
    private final Formatter<Object> defaultFormatter;

    @Inject
    public FormatterRegistry(final Injector injector) {
        this.defaultFormatter = injector.getInstance(DefaultFormatter.class);
        this.formatterMap.put(BackupArchetype.class, injector.getInstance(BackupArchetypeFormatter.class));
        this.formatterMap.put(Component.class, injector.getInstance(ComponentFormatter.class));
        this.formatterMap.put(Key.class, injector.getInstance(KeyFormatter.class));
        this.formatterMap.put(Keyed.class, injector.getInstance(KeyedFormatter.class));
        this.formatterMap.put(Objective.class, injector.getInstance(ObjectiveFormatter.class));
        this.formatterMap.put(Parameter.Key.class, injector.getInstance(ParameterKeyFormatter.class));
        this.formatterMap.put(ServerLocation.class, injector.getInstance(ServerLocationFormatter.class));
        this.formatterMap.put(ServerWorld.class, injector.getInstance(ServerWorldFormatter.class));
        this.formatterMap.put(String.class, injector.getInstance(StringFormatter.class));
        this.formatterMap.put(Team.class, injector.getInstance(TeamFormatter.class));
        this.formatterMap.put(Vector2d.class, injector.getInstance(Vector2dFormatter.class));
        this.formatterMap.put(Vector3d.class, injector.getInstance(Vector3dFormatter.class));
    }

    @Override
    public Formatter<?> get(final Class<?> key) {
        return this.formatterMap.get(key);
    }

    @Override
    public boolean has(final Class<?> key) {
        return this.formatterMap.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<Formatter<? super T>> getSuper(final Class<T> key) {
        return this.formatterMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(key))
                .findFirst()
                .map(entry -> (Formatter<? super T>) entry.getValue());
    }

    public Formatter<Object> getDefaultFormatter() {
        return this.defaultFormatter;
    }

}
