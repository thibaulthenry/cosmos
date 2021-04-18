package cosmos.statics.serializers;

import cosmos.constants.DefaultData;
import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

public class HealthsSerializer {

    public static void serialize(Path path, HealthData healthData) {
        FinderFile.writeToFile(healthData.toContainer(), path);
    }

    public static void serializePlayerData(Path path, Path inputPath) {
        DataContainer dataContainer = DataContainer.createNew();

        DataQuery healthQuery = DataQuery.of("Health");
        DataQuery attributesQuery = DataQuery.of("Attributes");
        DataQuery attributesBaseQuery = DataQuery.of("Base");
        DataQuery attributesNameQuery = DataQuery.of("Name");

        FinderFile.readFromNbtFile(inputPath).ifPresent(playerDataContainer -> {
            if (!playerDataContainer.contains(attributesQuery, healthQuery)) {
                return;
            }

            playerDataContainer.getFloat(healthQuery)
                    .map(Float::doubleValue)
                    .ifPresent(value -> dataContainer.set(Keys.HEALTH, value));

            playerDataContainer.getViewList(attributesQuery).orElse(Collections.emptyList())
                    .forEach(attributeView -> {
                        if (!attributeView.contains(attributesBaseQuery, attributesNameQuery)) {
                            return;
                        }

                        attributeView.getString(attributesNameQuery)
                                .filter("generic.maxHealth"::equals)
                                .flatMap(value -> attributeView.getDouble(attributesBaseQuery))
                                .ifPresent(value -> dataContainer.set(Keys.MAX_HEALTH, value));
                    });
        });

        if (!dataContainer.contains(Keys.HEALTH, Keys.MAX_HEALTH)) {
            return;
        }

        Sponge.getDataManager()
                .deserialize(HealthData.class, dataContainer)
                .ifPresent(data -> serialize(path, data));
    }

    public static Optional<HealthData> deserialize(Path path) {
        DataContainer dataContainer = FinderFile.readFromNbtFile(path).orElse(DefaultData.DEFAULT_HEALTH_DATA);
        return Sponge.getDataManager().deserialize(HealthData.class, dataContainer);
    }

}
