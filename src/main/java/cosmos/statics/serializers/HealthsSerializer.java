package cosmos.statics.serializers;

import cosmos.constants.DefaultData;
import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;

import java.nio.file.Path;
import java.util.Optional;

public class HealthsSerializer {

    public static void serialize(Path path, HealthData healthData) {
        FinderFile.writeToFile(healthData.toContainer(), path);
    }

    public static Optional<HealthData> deserialize(Path path) {
        DataContainer dataContainer = FinderFile.readFromFile(path).orElse(DefaultData.DEFAULT_HEALTH_DATA);
        return Sponge.getDataManager().deserialize(HealthData.class, dataContainer);
    }
}
