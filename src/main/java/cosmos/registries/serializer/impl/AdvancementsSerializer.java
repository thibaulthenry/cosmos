package cosmos.registries.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.AdvancementTreeData;
import cosmos.registries.serializer.Serializer;
import cosmos.services.io.FinderService;
import cosmos.services.perworld.InventoriesService;
import cosmos.services.serializer.SerializerProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class AdvancementsSerializer implements Serializer<AdvancementTreeData> {

    private final FinderService finderService;

    @Inject
    public AdvancementsSerializer(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
    }

    @Override
    public void serialize(final Path path, final AdvancementTreeData data) {
        this.finderService.writeToFile(data, path);
    }

    @Override
    public Optional<AdvancementTreeData> deserialize(final Path path) {
        final DataContainer dataContainer = this.finderService.readFromFile(path).orElse(DataContainer.createNew());

        if (dataContainer.isEmpty()) {
            return Optional.empty();
        }

        return Sponge.getDataManager().deserialize(AdvancementTreeData.class, dataContainer);
    }

}
