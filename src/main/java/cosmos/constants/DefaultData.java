package cosmos.constants;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;

public class DefaultData {

    public static final DataContainer DEFAULT_HEALTH_DATA =
            DataContainer.createNew(DataView.SafetyMode.ALL_DATA_CLONED)
                    .set(Keys.HEALTH, 20.0)
                    .set(Keys.MAX_HEALTH, 20.0);

    public static final DataContainer DEFAULT_FOOD_DATA =
            DataContainer.createNew(DataView.SafetyMode.ALL_DATA_CLONED)
                    .set(Keys.SATURATION, 5.0)
                    .set(Keys.FOOD_LEVEL, 20)
                    .set(Keys.EXHAUSTION, 0.0);

    public static final DataContainer DEFAULT_EXPERIENCE_HOLDER_DATA =
            DataContainer.createNew(DataView.SafetyMode.ALL_DATA_CLONED)
                    .set(Keys.EXPERIENCE_SINCE_LEVEL, 0)
                    .set(Keys.TOTAL_EXPERIENCE, 0)
                    .set(Keys.EXPERIENCE_LEVEL, 0);
}
