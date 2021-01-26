package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.HungerData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class HungerDataBuilder extends AbstractDataBuilder<HungerData> {

    @Inject
    public HungerDataBuilder() {
        this(HungerData.class, 1);
    }

    protected HungerDataBuilder(final Class<HungerData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<HungerData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Hungers.EXHAUSTION, Queries.Hungers.FOOD_LEVEL,
                Queries.Hungers.MAX_EXHAUSTION, Queries.Hungers.MAX_FOOD_LEVEL, Queries.Hungers.SATURATION)) {
            return Optional.empty();
        }

        final double exhaustion = container.getInt(Queries.Hungers.EXHAUSTION)
                .orElseThrow(() -> new InvalidDataException("Missing exhaustion while building HungerData"));
        final int foodLevel = container.getInt(Queries.Hungers.FOOD_LEVEL)
                .orElseThrow(() -> new InvalidDataException("Missing food level building HungerData"));
        final double maxExhaustion = container.getInt(Queries.Hungers.MAX_EXHAUSTION)
                .orElseThrow(() -> new InvalidDataException("Missing max exhaustion building HungerData"));
        final int maxFoodLevel = container.getInt(Queries.Hungers.MAX_FOOD_LEVEL)
                .orElseThrow(() -> new InvalidDataException("Missing max food level while building HungerData"));
        final double saturation = container.getInt(Queries.Hungers.SATURATION)
                .orElseThrow(() -> new InvalidDataException("Missing saturation while building HungerData"));

        return Optional.of(new HungerData(exhaustion, foodLevel, maxExhaustion, maxFoodLevel, saturation));
    }

}
