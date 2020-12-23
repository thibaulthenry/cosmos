package cosmos.services.io.impl;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.services.io.FinderService;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataView;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class FinderServiceImpl implements FinderService {

    public void writeToFile(final DataView dataContainer, final Path path) {
        if (dataContainer == null || dataContainer.isEmpty()) {
            return;
        }

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            DataFormats.NBT.get().writeTo(outputStream, dataContainer);
        } catch (Exception ignored) {
            Cosmos.getLogger().error("An error occurred while saving data container at " + path);
        }
    }

    public Optional<DataContainer> readFromFile(final Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            final DataContainer dataContainer = DataFormats.NBT.get().readFrom(inputStream);

            if (dataContainer.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(dataContainer);
        } catch (Exception ignored) {
            Cosmos.getLogger().error("An error occurred while reading data container at " + path);
            return Optional.empty();
        }
    }
}
