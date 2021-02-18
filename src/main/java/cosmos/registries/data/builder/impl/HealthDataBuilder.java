package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.HealthData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class HealthDataBuilder extends AbstractDataBuilder<HealthData> {

    @Inject
    public HealthDataBuilder() {
        this(HealthData.class, 1);
    }

    protected HealthDataBuilder(final Class<HealthData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<HealthData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Healths.HEALTH, Queries.Healths.MAX_HEALTH, Queries.Healths.ABSORPTION)) {
            return Optional.empty();
        }

        final double absorption = container.getDouble(Queries.Healths.ABSORPTION)
                .orElseThrow(() -> new InvalidDataException("Missing absorption while building HealthData"));

        final double health = container.getDouble(Queries.Healths.HEALTH)
                .orElseThrow(() -> new InvalidDataException("Missing health while building HealthData"));

        final double maxHealth = container.getDouble(Queries.Healths.MAX_HEALTH)
                .orElseThrow(() -> new InvalidDataException("Missing max health while building HealthData"));

        return Optional.of(new HealthData(absorption, health, maxHealth));
    }

}
