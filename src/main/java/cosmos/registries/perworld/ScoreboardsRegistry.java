package cosmos.registries.perworld;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ScoreboardsRegistry implements CosmosRegistry<ResourceKey, Scoreboard> {

    private final Map<ResourceKey, Scoreboard> scoreboardMap = new HashMap<>();

    @Override
    public Scoreboard get(final ResourceKey key) {
        return this.scoreboardMap.get(key);
    }

    public Scoreboard getOrCreate(final ResourceKey key) {
        return this.scoreboardMap.computeIfAbsent(key, k -> Scoreboard.builder().build());
    }

    @Override
    public boolean has(final ResourceKey key) {
        return this.scoreboardMap.containsKey(key);
    }

    public Collection<Scoreboard> values() {
        return this.scoreboardMap.values();
    }

}
