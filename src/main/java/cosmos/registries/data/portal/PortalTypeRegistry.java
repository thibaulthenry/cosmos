package cosmos.registries.data.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.DataKeys;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.data.portal.impl.CosmosFramePortalType;
import org.spongepowered.api.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class PortalTypeRegistry implements CosmosRegistry<ResourceKey, CosmosPortalType> {

    private final Map<ResourceKey, CosmosPortalType> portalTypeMap = new HashMap<>();

    @Inject
    public PortalTypeRegistry(final Injector injector) {
        this.portalTypeMap.put(DataKeys.PORTAL_TYPE_FRAME, injector.getInstance(CosmosFramePortalType.class));
    }

    public Stream<CosmosRegistryEntry<ResourceKey, CosmosPortalType>> streamEntries() {
        return this.portalTypeMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public CosmosPortalType value(final ResourceKey key) {
        return this.portalTypeMap.get(key);
    }

}
