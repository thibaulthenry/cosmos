package cosmos.statics.serializers;

import cosmos.constants.DefaultData;
import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.ExperienceHolderData;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ExperiencesSerializer {

    public static void serialize(Path path, ExperienceHolderData experienceHolderData) {
        FinderFile.writeToFile(experienceHolderData.toContainer(), path);
    }

    public static void serializePlayerData(List<Path> savedPath, Path inputPath) {
        if (savedPath.isEmpty()) {
            return;
        }

        DataContainer dataContainer = DataContainer.createNew();

        DataQuery experienceTotalQuery = DataQuery.of("XpTotal");
        DataQuery experienceLevelQuery = DataQuery.of("XpLevel");

        FinderFile.readFromNbtFile(inputPath).ifPresent(playerDataContainer -> {
            if (!playerDataContainer.contains(experienceLevelQuery, experienceTotalQuery)) {
                return;
            }

            Optional<Integer> optionalExperienceTotal = playerDataContainer.getInt(experienceTotalQuery);
            Optional<Integer> optionalExperienceLevel = playerDataContainer.getInt(experienceLevelQuery);

            if (!optionalExperienceTotal.isPresent() || !optionalExperienceLevel.isPresent()) {
                return;
            }

            int experienceTotal = optionalExperienceTotal.get();
            int experienceLevel = optionalExperienceLevel.get();
            // See org.spongepowered.common.mixin.core.entity.player.EntityPlayerMixin#bridge$getExperienceSinceLevel
            int experienceSinceLevel = experienceTotal - xpAtLevel(experienceLevel);

            dataContainer
                    .set(Keys.TOTAL_EXPERIENCE, experienceTotal)
                    .set(Keys.EXPERIENCE_LEVEL, experienceLevel)
                    .set(Keys.EXPERIENCE_SINCE_LEVEL, experienceSinceLevel);
        });

        if (!dataContainer.contains(Keys.TOTAL_EXPERIENCE, Keys.EXPERIENCE_LEVEL, Keys.EXPERIENCE_SINCE_LEVEL)) {
            return;
        }

        savedPath.forEach(path ->
                Sponge.getDataManager()
                        .deserialize(ExperienceHolderData.class, dataContainer)
                        .ifPresent(data -> serialize(path, data))
        );
    }

    public static Optional<ExperienceHolderData> deserialize(Path path) {
        DataContainer dataContainer = FinderFile.readFromNbtFile(path).orElse(DefaultData.DEFAULT_EXPERIENCE_HOLDER_DATA);
        return Sponge.getDataManager().deserialize(ExperienceHolderData.class, dataContainer);
    }

    // See org.spongepowered.common.mixin.core.entity.player.EntityPlayerMixin#xpAtLevel
    public static int xpAtLevel(final int level) {
        if (level > 30) {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        } else if (level > 15) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return level * level + 6 * level;
        }
    }

}
