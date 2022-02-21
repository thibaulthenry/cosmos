package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import org.spongepowered.api.data.persistence.DataContainer;

public class ScoreCriterionProgressData extends CriterionProgressData {

    private final int goal;
    private final int score;

    public ScoreCriterionProgressData(final String date, final int goal, final String name, final int score) {
        super(date, name);
        this.goal = goal;
        this.score = score;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(Queries.Advancement.Criterion.GOAL, this.goal)
                .set(Queries.Advancement.Criterion.SCORE, this.score);
    }

}
