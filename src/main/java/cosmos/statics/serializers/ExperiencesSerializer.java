package cosmos.statics.serializers;

import cosmos.constants.DefaultData;
import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.mutable.entity.ExperienceHolderData;

import java.nio.file.Path;
import java.util.Optional;

public class ExperiencesSerializer {

    public static void serialize(Path path, ExperienceHolderData experienceHolderData) {
        FinderFile.writeToFile(experienceHolderData.toContainer(), path);
    }

    public static Optional<ExperienceHolderData> deserialize(Path path) {
        DataContainer dataContainer = FinderFile.readFromFile(path).orElse(DefaultData.DEFAULT_EXPERIENCE_HOLDER_DATA);
        return Sponge.getDataManager().deserialize(ExperienceHolderData.class, dataContainer);
    }

}
