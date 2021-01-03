package cosmos.registries.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.AdvancementTreeData;
import cosmos.registries.serializer.Serializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class AdvancementsSerializer implements Serializer<AdvancementTreeData> {

    @Inject
    private FinderService finderService;

    @Override
    public void serialize(final Path path, final AdvancementTreeData data) {
        this.finderService.writeToFile(data.toContainer(), path);
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
