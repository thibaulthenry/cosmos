package cosmos.registries.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.HungerData;
import cosmos.registries.serializer.Serializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class HungersSerializer implements Serializer<HungerData> {

    @Inject
    private FinderService finderService;

    @Override
    public void serialize(final Path path, final HungerData data) {
        this.finderService.writeToFile(data.toContainer(), path);
    }

    @Override
    public Optional<HungerData> deserialize(final Path path) {
        final DataContainer dataContainer = this.finderService.readFromFile(path).orElse(new HungerData().toContainer());

        if (dataContainer.isEmpty()) {
            return Optional.empty();
        }

        return Sponge.getDataManager().deserialize(HungerData.class, dataContainer);
    }
}
