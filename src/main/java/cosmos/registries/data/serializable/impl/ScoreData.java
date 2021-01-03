package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;

public class ScoreData implements DataSerializable {

    private final boolean locked;
    private final String objective;
    private final int score;
    private final String targetName;

    public ScoreData(final Score score, final Objective objective) {
        this.locked = score.isLocked();
        this.objective = objective.getName();
        this.score = score.getScore();
        this.targetName = GsonComponentSerializer.gson().serialize(score.getName());
    }

    public ScoreData(final boolean locked, final String objective, final int score, final String targetName) {
        this.locked = locked;
        this.objective = objective;
        this.score = score;
        this.targetName = targetName;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboards.Score.LOCKED, this.locked)
                .set(Queries.Scoreboards.Score.OBJECTIVE, this.objective)
                .set(Queries.Scoreboards.Score.SCORE, this.score)
                .set(Queries.Scoreboards.Score.TARGET_NAME, this.targetName);
    }
}
