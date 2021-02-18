package cosmos.registries.portal;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.portal.impl.PortalTeleportTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class PortalTeleportTaskRegistry implements CosmosRegistry<UUID, PortalTeleportTask> {

    private final Map<UUID, PortalTeleportTask> portalTeleportTaskMap = new HashMap<>();

    @Override
    public Optional<CosmosRegistryEntry<UUID, PortalTeleportTask>> register(final UUID key, final PortalTeleportTask value) {
        return Optional.ofNullable(this.portalTeleportTaskMap.computeIfAbsent(key, k -> value)).map(v ->  CosmosRegistryEntry.of(key, v));
    }

    @Override
    public Optional<CosmosRegistryEntry<UUID, PortalTeleportTask>> unregister(final UUID key) {
        return Optional.ofNullable(this.portalTeleportTaskMap.remove(key)).map(v -> CosmosRegistryEntry.of(key, v));
    }

    @Override
    public PortalTeleportTask value(final UUID key) {
        return this.portalTeleportTaskMap.get(key);
    }

}
