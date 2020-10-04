package cosmos.statics.arguments.implementations.scoreboard;

import cosmos.listeners.perworld.ScoreboardsListener;
import cosmos.statics.finders.FinderScoreboard;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ObjectiveChoiceElement extends AbstractScoreboardChoiceElement<Objective> {

    private final String[] acceptedCriterionIds;

    public ObjectiveChoiceElement(@Nullable Text key, @Nonnull Text worldKey, Criterion... acceptedCriteria) {
        super(key, worldKey);
        acceptedCriterionIds = Arrays.stream(acceptedCriteria).map(Criterion::getId).toArray(String[]::new);
    }

    @Override
    void setKeySupplier(WorldProperties worldProperties) {
        keySupplier = () -> FinderScoreboard.getWorldObjectives(worldProperties.getUniqueId(), acceptedCriterionIds)
                .stream()
                .map(Objective::getName)
                .collect(Collectors.toList());
    }

    @Override
    void setValueSupplier(WorldProperties worldProperties) {
        valueSupplier = (key) -> ScoreboardsListener
                .getScoreboard(worldProperties.getUniqueId())
                .getObjective(key)
                .orElse(null);
    }
}
