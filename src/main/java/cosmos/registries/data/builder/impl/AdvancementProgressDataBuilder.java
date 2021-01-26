package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.AdvancementProgressData;
import cosmos.registries.data.serializable.impl.CriterionProgressData;
import cosmos.registries.data.serializable.impl.ScoreCriterionProgressData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AdvancementProgressDataBuilder extends AbstractDataBuilder<AdvancementProgressData> {

    @Inject
    public AdvancementProgressDataBuilder() {
        this(AdvancementProgressData.class, 1);
    }

    protected AdvancementProgressDataBuilder(final Class<AdvancementProgressData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<AdvancementProgressData> buildContent(final DataView container) throws InvalidDataException {
        final List<CriterionProgressData> criteriaProgressesData = container.getKeys(false)
                .stream()
                .map(container::getView)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(view -> {
                    if (view.getKeys(false).size() > 2) {
                        return Sponge.getDataManager().deserialize(ScoreCriterionProgressData.class, view);
                    }

                    return Sponge.getDataManager().deserialize(CriterionProgressData.class, view);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return Optional.of(new AdvancementProgressData(criteriaProgressesData, container.getName()));
    }

}
