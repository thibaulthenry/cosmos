package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.ScoreData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class ScoreDataBuilder extends AbstractDataBuilder<ScoreData> {

    @Inject
    public ScoreDataBuilder() {
        this(ScoreData.class, 1);
    }

    protected ScoreDataBuilder(final Class<ScoreData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<ScoreData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Scoreboard.Score.LOCKED, Queries.Scoreboard.Score.OBJECTIVE,
                Queries.Scoreboard.Score.SCORE, Queries.Scoreboard.Score.TARGET_NAME)) {
            return Optional.empty();
        }

        final boolean locked = container.getBoolean(Queries.Scoreboard.Score.LOCKED)
                .orElseThrow(() -> new InvalidDataException("Missing locked while building ScoreData"));

        final String objective = container.getString(Queries.Scoreboard.Score.OBJECTIVE)
                .orElseThrow(() -> new InvalidDataException("Missing objective while building ScoreData"));

        final int score = container.getInt(Queries.Scoreboard.Score.SCORE)
                .orElseThrow(() -> new InvalidDataException("Missing score while building ScoreData"));

        final String targetName = container.getString(Queries.Scoreboard.Score.TARGET_NAME)
                .orElseThrow(() -> new InvalidDataException("Missing target name while building ScoreData"));

        return Optional.of(new ScoreData(locked, objective, score, targetName));
    }

}
