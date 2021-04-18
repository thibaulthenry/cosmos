package cosmos.listeners.perworld;

import cosmos.listeners.AbstractListener;
import cosmos.listeners.ToggleListener;
import org.spongepowered.api.data.persistence.DataFormat;
import org.spongepowered.api.data.persistence.DataFormats;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractPerWorldListener extends AbstractListener implements ToggleListener {

    public Optional<UUID> extractUUIDFromFile(Path filePath, DataFormat dataFormat) {
        String extension;

        if (DataFormats.NBT.equals(dataFormat)) {
            extension = ".dat";
        } else if (DataFormats.JSON.equals(dataFormat)) {
            extension = ".json";
        } else if (DataFormats.HOCON.equals(dataFormat)) {
            extension = ".conf";
        } else {
            return Optional.empty();
        }

        UUID uuid;

        try {
            String fileName = filePath.getFileName().toString();

            if (!fileName.endsWith(extension)) {
                return Optional.empty();
            }

            int index = fileName.lastIndexOf('.');

            if (index == -1) {
                uuid = UUID.fromString(fileName);
            } else {
                uuid = UUID.fromString(fileName.substring(0, index));
            }

            return Optional.of(uuid);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

}
