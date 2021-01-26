package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.ScoreCriterionProgressData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class ScoreCriterionProgressDataBuilder extends AbstractDataBuilder<ScoreCriterionProgressData> {

    @Inject
    public ScoreCriterionProgressDataBuilder() {
        this(ScoreCriterionProgressData.class, 1);
    }

    protected ScoreCriterionProgressDataBuilder(final Class<ScoreCriterionProgressData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<ScoreCriterionProgressData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Advancements.Criterion.DATE, Queries.Advancements.Criterion.GOAL,
                Queries.Advancements.Criterion.SCORE)) {
            return Optional.empty();
        }

        final String date = container.getString(Queries.Advancements.Criterion.DATE)
                .orElseThrow(() -> new InvalidDataException("Missing date while building ScoreCriterionProgressData"));
        final int goal = container.getInt(Queries.Advancements.Criterion.GOAL)
                .orElseThrow(() -> new InvalidDataException("Missing goal while building ScoreCriterionProgressData"));
        final String name = container.getParent()
                .map(DataView::getName)
                .orElseThrow(() -> new InvalidDataException("Missing name while building ScoreCriterionProgressData"));
        final int score = container.getInt(Queries.Advancements.Criterion.SCORE)
                .orElseThrow(() -> new InvalidDataException("Missing score while building ScoreCriterionProgressData"));

        return Optional.of(new ScoreCriterionProgressData(date, goal, name, score));
    }

}
