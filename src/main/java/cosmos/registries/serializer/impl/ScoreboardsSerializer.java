package cosmos.registries.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.ScoreboardData;
import cosmos.registries.serializer.Serializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class ScoreboardsSerializer implements Serializer<ScoreboardData> {

    @Inject
    private FinderService finderService;

    @Override
    public void serialize(final Path path, final ScoreboardData data) {
        this.finderService.writeToFile(data.toContainer(), path);
    }

    @Override
    public Optional<ScoreboardData> deserialize(final Path path) {
        final DataContainer dataContainer = this.finderService.readFromFile(path).orElse(DataContainer.createNew());

        if (dataContainer.isEmpty()) {
            return Optional.empty();
        }

        return Sponge.getDataManager().deserialize(ScoreboardData.class, dataContainer);
    }
}
