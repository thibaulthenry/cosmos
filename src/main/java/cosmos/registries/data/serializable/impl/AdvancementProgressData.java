package cosmos.registries.data.serializable.impl;

import org.spongepowered.api.advancement.Advancement;
import org.spongepowered.api.advancement.AdvancementProgress;
import org.spongepowered.api.advancement.criteria.AdvancementCriterion;
import org.spongepowered.api.advancement.criteria.CriterionProgress;
import org.spongepowered.api.advancement.criteria.OperatorCriterion;
import org.spongepowered.api.advancement.criteria.ScoreCriterionProgress;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.data.persistence.DataView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AdvancementProgressData implements DataSerializable {

    private final List<CriterionProgressData> criteriaProgressesData;
    private final String key;

    public AdvancementProgressData(final AdvancementProgress advancementProgress) {
        final Advancement advancement = advancementProgress.getAdvancement();
        final AdvancementCriterion advancementCriterion = advancement.getCriterion();

        this.criteriaProgressesData = this.getCriterionProgresses(advancementProgress, advancementCriterion)
                .stream()
                .filter(this::isCriterionProgressStarted)
                .map(CriterionProgressData::new)
                .collect(Collectors.toList());
        this.key = advancement.getKey().getFormatted();
    }

    public AdvancementProgressData(final List<CriterionProgressData> criteriaProgressesData, final String key) {
        this.criteriaProgressesData = criteriaProgressesData;
        this.key = key;
    }

    private Collection<CriterionProgress> getCriterionProgresses(final AdvancementProgress advancementProgress, final AdvancementCriterion rootCriterion) {
        if (!(rootCriterion instanceof OperatorCriterion)) {
            return advancementProgress.get(rootCriterion)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }

        return ((OperatorCriterion) rootCriterion).getCriteria()
                .stream()
                .map(advancementCriterion -> {
                    if (advancementCriterion instanceof OperatorCriterion) {
                        return this.getCriterionProgresses(advancementProgress, advancementCriterion);
                    }

                    return advancementProgress.get(advancementCriterion)
                            .map(Collections::singletonList)
                            .orElse(Collections.emptyList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public String getKey() {
        return this.key;
    }

    public boolean isAdvancementStarted() {
        return !this.criteriaProgressesData.isEmpty();
    }

    private boolean isCriterionProgressStarted(final CriterionProgress criterionProgress) {
        if (criterionProgress instanceof ScoreCriterionProgress) {
            return ((ScoreCriterionProgress) criterionProgress).getScore() > 0;
        }

        return criterionProgress.achieved();
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer dataContainer = DataContainer.createNew(DataView.SafetyMode.NO_DATA_CLONED);

        this.criteriaProgressesData.forEach(data -> dataContainer.set(DataQuery.of(data.getName()), data));

        return dataContainer;
    }

}
