package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;

public class ScoreData implements ShareableSerializable<Scoreboard> {

    private final boolean locked;
    private final String objective;
    private final int score;
    private final String targetName;

    public ScoreData(final Score score, final Objective objective) {
        this.locked = score.isLocked();
        this.objective = objective.name();
        this.score = score.score();
        this.targetName = GsonComponentSerializer.gson().serialize(score.name());
    }

    public ScoreData(final boolean locked, final String objective, final int score, final String targetName) {
        this.locked = locked;
        this.objective = objective;
        this.score = score;
        this.targetName = targetName;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public void share(final Scoreboard data) {
        final Component target = GsonComponentSerializer.gson().deserialize(this.targetName);

        data.objective(this.objective).ifPresent(objective -> {
            final Score score = objective.findOrCreateScore(target);

            score.setScore(this.score);
            score.setLocked(this.locked);
        });
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboard.Score.LOCKED, this.locked)
                .set(Queries.Scoreboard.Score.OBJECTIVE, this.objective)
                .set(Queries.Scoreboard.Score.SCORE, this.score)
                .set(Queries.Scoreboard.Score.TARGET_NAME, this.targetName);
    }

}
