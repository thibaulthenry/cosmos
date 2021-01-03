package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import org.spongepowered.api.advancement.criteria.CriterionProgress;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CriterionProgressData implements DataSerializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault()); // todo better ?

    private final String date;
    private final String name;

    public CriterionProgressData(final CriterionProgress criterionProgress) {
        this.date = CriterionProgressData.DATE_TIME_FORMATTER.format(criterionProgress.get().orElse(Instant.now()));
        this.name = criterionProgress.getCriterion().getName();
    }

    public CriterionProgressData(final String date, final String name) {
        this.date = date;
        this.name = name;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew().set(Queries.Advancements.Criterion.DATE, this.date);
    }
}
