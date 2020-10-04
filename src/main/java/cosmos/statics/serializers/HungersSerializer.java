package cosmos.statics.serializers;

import cosmos.constants.DefaultData;
import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;

import java.nio.file.Path;
import java.util.Optional;

public class HungersSerializer {

    public static void serialize(Path path, FoodData foodData) {
        FinderFile.writeToFile(foodData.toContainer(), path);
    }

    public static Optional<FoodData> deserialize(Path path) {
        DataContainer dataContainer = FinderFile.readFromFile(path).orElse(DefaultData.DEFAULT_FOOD_DATA);
        return Sponge.getDataManager().deserialize(FoodData.class, dataContainer);
    }
}
