package cosmos.statics.serializers;

import cosmos.constants.DefaultData;
import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;

import java.nio.file.Path;
import java.util.Optional;

public class HungersSerializer {

    public static void serialize(Path path, FoodData foodData) {
        FinderFile.writeToFile(foodData.toContainer(), path);
    }

    public static void serializePlayerData(Path path, Path inputPath) {
        DataContainer dataContainer = DataContainer.createNew();

        DataQuery exhaustionQuery = DataQuery.of("foodExhaustionLevel");
        DataQuery foodLevelQuery = DataQuery.of("foodLevel");
        DataQuery saturationQuery = DataQuery.of("foodSaturationLevel");

        FinderFile.readFromNbtFile(inputPath).ifPresent(playerDataContainer -> {
            if (!playerDataContainer.contains(exhaustionQuery, foodLevelQuery, saturationQuery)) {
                return;
            }

            playerDataContainer.getFloat(exhaustionQuery)
                    .map(Float::doubleValue)
                    .ifPresent(value -> dataContainer.set(Keys.EXHAUSTION, value));

            playerDataContainer.getInt(foodLevelQuery)
                    .ifPresent(value -> dataContainer.set(Keys.FOOD_LEVEL, value));

            playerDataContainer.getFloat(saturationQuery)
                    .map(Float::doubleValue)
                    .ifPresent(value -> dataContainer.set(Keys.SATURATION, value));

        });

        if (!dataContainer.contains(Keys.EXHAUSTION, Keys.FOOD_LEVEL, Keys.SATURATION)) {
            return;
        }

        Sponge.getDataManager()
                .deserialize(FoodData.class, dataContainer)
                .ifPresent(data -> serialize(path, data));
    }

    public static Optional<FoodData> deserialize(Path path) {
        DataContainer dataContainer = FinderFile.readFromNbtFile(path).orElse(DefaultData.DEFAULT_FOOD_DATA);
        return Sponge.getDataManager().deserialize(FoodData.class, dataContainer);
    }
}
