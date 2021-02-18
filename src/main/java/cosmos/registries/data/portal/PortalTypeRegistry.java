package cosmos.registries.data.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.DataKeys;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.data.portal.impl.CosmosButtonPortalType;
import cosmos.registries.data.portal.impl.CosmosFramePortalType;
import cosmos.registries.data.portal.impl.CosmosPressurePlatePortalType;
import cosmos.registries.data.portal.impl.CosmosSignPortalType;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.portal.PortalType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class PortalTypeRegistry implements CosmosRegistry<ResourceKey, CosmosPortalType> {

    private final Map<ResourceKey, CosmosPortalType> portalTypeMap = new HashMap<>();

    @Inject
    public PortalTypeRegistry(final Injector injector) {
        this.portalTypeMap.put(DataKeys.Portal.Type.BUTTON, injector.getInstance(CosmosButtonPortalType.class));
        this.portalTypeMap.put(DataKeys.Portal.Type.FRAME, injector.getInstance(CosmosFramePortalType.class));
        this.portalTypeMap.put(DataKeys.Portal.Type.PRESSURE_PLATE, injector.getInstance(CosmosPressurePlatePortalType.class));
        this.portalTypeMap.put(DataKeys.Portal.Type.SIGN, injector.getInstance(CosmosSignPortalType.class));
    }

    public void registerAll() {
        final Registry<PortalType> registry = RegistryTypes.PORTAL_TYPE.get();
        this.streamEntries().forEach(entry -> registry.register(entry.key(), entry.value()));
    }

    @Override
    public Stream<CosmosRegistryEntry<ResourceKey, CosmosPortalType>> streamEntries() {
        return this.portalTypeMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public CosmosPortalType value(final ResourceKey key) {
        return this.portalTypeMap.get(key);
    }

}
