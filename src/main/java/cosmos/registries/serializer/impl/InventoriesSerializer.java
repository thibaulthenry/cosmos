package cosmos.registries.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.serializer.Serializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class InventoriesSerializer implements Serializer<ExtendedInventoryData> {

    private final FinderService finderService;

    @Inject
    public InventoriesSerializer(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
    }

    @Override
    public void serialize(final Path path, final ExtendedInventoryData data) {
        this.finderService.writeToFile(data, path);
    }

    @Override
    public Optional<ExtendedInventoryData> deserialize(final Path path) {
        final DataContainer dataContainer = this.finderService.readFromFile(path).orElse(DataContainer.createNew());

        if (dataContainer.isEmpty()) {
            return Optional.empty();
        }

        return Sponge.getDataManager().deserialize(ExtendedInventoryData.class, dataContainer);
    }

}
