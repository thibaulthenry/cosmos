package cosmos.registries.portal;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Singleton
public class PortalSelectionRegistry implements CosmosRegistry<UUID, Set<ServerLocation>> {

    private final Map<UUID, Set<ServerLocation>> portalSelectionMap = new HashMap<>();

    public boolean add(final UUID key, final ServerLocation location) {
        return this.portalSelectionMap.computeIfAbsent(key, k -> new HashSet<>()).add(location);
    }

    public boolean remove(final UUID key, final ServerLocation location) {
        return this.portalSelectionMap.computeIfAbsent(key, k -> new HashSet<>()).remove(location);
    }

    @Override
    public Stream<CosmosRegistryEntry<UUID, Set<ServerLocation>>> streamEntries() {
        return this.portalSelectionMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public Optional<CosmosRegistryEntry<UUID, Set<ServerLocation>>> unregister(final UUID key) {
        return Optional.ofNullable(this.portalSelectionMap.remove(key)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public Set<ServerLocation> value(final UUID key) {
        return this.portalSelectionMap.get(key);
    }

}
