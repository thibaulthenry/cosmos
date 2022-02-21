package cosmos.registries.perworld;

import com.google.inject.Singleton;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.CosmosRegistry;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Tuple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Singleton
public class BypassRegistry implements CosmosRegistry<PerWorldFeatures, Set<Tuple<UUID, String>>> {

    private final Map<PerWorldFeatures, Set<Tuple<UUID, String>>> bypassMap = new HashMap<>();

    public boolean doesBypass(final PerWorldFeatures feature, final ServerPlayer player) {
        return this.bypassMap.containsKey(feature) && this.bypassMap.get(feature).contains(Tuple.of(player.uniqueId(), player.name()));
    }

    public boolean add(final PerWorldFeatures key, final ServerPlayer player) {
        return this.bypassMap.computeIfAbsent(key, k -> new HashSet<>()).add(Tuple.of(player.uniqueId(), player.name()));
    }

    public boolean remove(final PerWorldFeatures key, final ServerPlayer player) {
        return this.bypassMap.computeIfAbsent(key, k -> new HashSet<>()).remove(Tuple.of(player.uniqueId(), player.name()));
    }

    @Override
    public Set<Tuple<UUID, String>> value(final PerWorldFeatures key) {
        return this.bypassMap.get(key);
    }

}
