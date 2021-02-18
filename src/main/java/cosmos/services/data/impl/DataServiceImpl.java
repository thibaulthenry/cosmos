package cosmos.services.data.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.builder.DataBuilderRegistry;
import cosmos.registries.data.portal.PortalTypeRegistry;
import cosmos.registries.data.selector.SelectorTypeRegistry;
import cosmos.services.data.DataService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.selector.SelectorType;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.portal.PortalType;

@Singleton
public class DataServiceImpl implements DataService {

    private final DataBuilderRegistry dataBuilderRegistry;
    private final PortalTypeRegistry portalTypeRegistry;
    private final SelectorTypeRegistry selectorTypeRegistry;

    @Inject
    public DataServiceImpl(final Injector injector) {
        this.dataBuilderRegistry = injector.getInstance(DataBuilderRegistry.class);
        this.portalTypeRegistry = injector.getInstance(PortalTypeRegistry.class);
        this.selectorTypeRegistry = injector.getInstance(SelectorTypeRegistry.class);

    }

    @SuppressWarnings("unchecked")
    public <T extends DataSerializable> void registerBuilders() {
        this.dataBuilderRegistry.streamEntries().forEach(entry ->
                Sponge.getDataManager().registerBuilder((Class<T>) entry.key(), (DataBuilder<T>) entry.value())
        );
    }

    @Override
    public void registerPortalTypes() {
        final Registry<PortalType> registry = RegistryTypes.PORTAL_TYPE.get();
        this.portalTypeRegistry.streamEntries().forEach(entry -> registry.register(entry.key(), entry.value()));
    }

    @Override
    public void registerSelectors() {
        final Registry<SelectorType> registry = RegistryTypes.SELECTOR_TYPE.get();
        this.selectorTypeRegistry.streamEntries().forEach(entry -> registry.register(entry.key(), entry.value()));
    }

}
