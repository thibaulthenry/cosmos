package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import org.spongepowered.api.advancement.criteria.CriterionProgress;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CriterionProgressData implements DataSerializable {

    private final String date;
    private final String name;

    public CriterionProgressData(final CriterionProgress criterionProgress) {
        this.date = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault()).format(criterionProgress.get().orElse(Instant.now()));
        this.name = criterionProgress.criterion().name();
    }

    public CriterionProgressData(final String date, final String name) {
        this.date = date;
        this.name = name;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    public String name() {
        return this.name;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew().set(Queries.Advancement.Criterion.DATE, this.date);
    }

}
