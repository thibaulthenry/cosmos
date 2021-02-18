package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.CriterionProgressData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class CriterionProgressDataBuilder extends AbstractDataBuilder<CriterionProgressData> {

    @Inject
    public CriterionProgressDataBuilder() {
        this(CriterionProgressData.class, 1);
    }

    protected CriterionProgressDataBuilder(final Class<CriterionProgressData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<CriterionProgressData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Advancements.Criterion.DATE)) {
            return Optional.empty();
        }

        final String date = container.getString(Queries.Advancements.Criterion.DATE)
                .orElseThrow(() -> new InvalidDataException("Missing date while building CriterionProgressData"));

        final String name = container.parent()
                .map(DataView::name)
                .orElseThrow(() -> new InvalidDataException("Missing name while building CriterionProgressData"));

        return Optional.of(new CriterionProgressData(date, name));
    }

}
