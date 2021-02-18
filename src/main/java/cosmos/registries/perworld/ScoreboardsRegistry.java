package cosmos.registries.perworld;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton
public class ScoreboardsRegistry implements CosmosRegistry<ResourceKey, Scoreboard> {

    private final Map<ResourceKey, Scoreboard> scoreboardMap = new HashMap<>();

    @Override
    public Optional<CosmosRegistryEntry<ResourceKey, Scoreboard>> register(final ResourceKey key, final Scoreboard value) {
        return Optional.ofNullable(this.scoreboardMap.computeIfAbsent(key, k -> value)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public Stream<CosmosRegistryEntry<ResourceKey, Scoreboard>> streamEntries() {
        return this.scoreboardMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public Scoreboard value(final ResourceKey key) {
        return this.scoreboardMap.get(key);
    }

}
