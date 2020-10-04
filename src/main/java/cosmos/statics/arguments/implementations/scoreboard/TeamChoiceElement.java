package cosmos.statics.arguments.implementations.scoreboard;

import cosmos.listeners.perworld.ScoreboardsListener;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Collectors;

public class TeamChoiceElement extends AbstractScoreboardChoiceElement<Team> {

    public TeamChoiceElement(@Nullable Text key, @Nonnull Text worldKey) {
        super(key, worldKey);
    }

    @Override
    void setKeySupplier(WorldProperties worldProperties) {
        keySupplier = () -> ScoreboardsListener
                .getScoreboard(worldProperties.getUniqueId())
                .getTeams()
                .stream()
                .map(Team::getName)
                .collect(Collectors.toList());
    }

    @Override
    void setValueSupplier(WorldProperties worldProperties) {
        valueSupplier = (key) -> ScoreboardsListener
                .getScoreboard(worldProperties.getUniqueId())
                .getTeam(key)
                .orElse(null);
    }
}
