package cosmos.registries.perworld;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Singleton
public class ScoreboardsRegistry implements CosmosRegistry<ResourceKey, Scoreboard> {

    private final Map<ResourceKey, Scoreboard> scoreboardMap = new HashMap<>();

    public Scoreboard computeIfAbsent(final ResourceKey key, final Function<ResourceKey, Scoreboard> mappingFunction) {
        return this.scoreboardMap.computeIfAbsent(key, mappingFunction);
    }

    public Set<Map.Entry<ResourceKey, Scoreboard>> entries() {
        return this.scoreboardMap.entrySet();
    }

    @Override
    public Scoreboard get(final ResourceKey key) {
        return this.scoreboardMap.get(key);
    }

    @Override
    public boolean has(final ResourceKey key) {
        return this.scoreboardMap.containsKey(key);
    }

    public Collection<Scoreboard> values() {
        return this.scoreboardMap.values();
    }

}
