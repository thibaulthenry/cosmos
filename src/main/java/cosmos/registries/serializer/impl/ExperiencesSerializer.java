package cosmos.registries.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.ExperienceData;
import cosmos.registries.serializer.Serializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class ExperiencesSerializer implements Serializer<ExperienceData> {

    private final FinderService finderService;

    @Inject
    public ExperiencesSerializer(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
    }

    @Override
    public Optional<ExperienceData> deserialize(Path path) {
        final DataContainer dataContainer = this.finderService.readFromFile(path).orElse(new ExperienceData().toContainer());

        if (dataContainer.isEmpty()) {
            return Optional.empty();
        }

        return Sponge.dataManager().deserialize(ExperienceData.class, dataContainer);
    }

    @Override
    public void serialize(final Path path, final ExperienceData data) {
        this.finderService.writeToFile(data, path);
    }

}
