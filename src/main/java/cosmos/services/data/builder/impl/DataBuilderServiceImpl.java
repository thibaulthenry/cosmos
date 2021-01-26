package cosmos.services.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.builder.DataBuilderRegistry;
import cosmos.services.data.builder.DataBuilderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.DataSerializable;

@Singleton
public class DataBuilderServiceImpl implements DataBuilderService {

    private final DataBuilderRegistry dataBuilderRegistry;

    @Inject
    public DataBuilderServiceImpl(final Injector injector) {
        this.dataBuilderRegistry = injector.getInstance(DataBuilderRegistry.class);
    }

    @SuppressWarnings("unchecked")
    public <T extends DataSerializable> void registerAll() {
        this.dataBuilderRegistry.entries().forEach(entry ->
                Sponge.getDataManager().registerBuilder((Class<T>) entry.getKey(), (DataBuilder<T>) entry.getValue())
        );
    }

}
