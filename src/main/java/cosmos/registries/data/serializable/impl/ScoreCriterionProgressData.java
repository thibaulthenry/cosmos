package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import org.spongepowered.api.advancement.criteria.ScoreCriterionProgress;
import org.spongepowered.api.data.persistence.DataContainer;

public class ScoreCriterionProgressData extends CriterionProgressData {

    private final int goal;
    private final int score;

    public ScoreCriterionProgressData(final ScoreCriterionProgress scoreCriterionProgress) {
        super(scoreCriterionProgress);
        this.goal = scoreCriterionProgress.getGoal();
        this.score = scoreCriterionProgress.getScore();
    }

    public ScoreCriterionProgressData(final String date, final int goal, final String name, final int score) {
        super(date, name);
        this.goal = goal;
        this.score = score;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(Queries.Advancements.Criterion.GOAL, this.goal)
                .set(Queries.Advancements.Criterion.SCORE, this.score);
    }
}
