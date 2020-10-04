package cosmos.statics.finders;

import cosmos.constants.ModifyCommands;
import cosmos.constants.TeamOptions;
import cosmos.listeners.perworld.ScoreboardsListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.statistic.Statistic;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinderScoreboard {

    public static Collection<ModifyCommands> getModifyCommands() {
        return Arrays.asList(ModifyCommands.values());
    }

    public static Collection<TeamOptions> getTeamOptions() {
        return Arrays.asList(TeamOptions.values());
    }

    public static Collection<Objective> getWorldObjectives(UUID worldUUID, String... criterionIds) {
        return ScoreboardsListener
                .getScoreboard(worldUUID)
                .getObjectives()
                .stream()
                .filter(worldObjective ->
                        criterionIds.length == 0 || Arrays
                                .stream(criterionIds)
                                .anyMatch(criterion -> criterion.equals(worldObjective.getCriterion().getId()))
                )
                .collect(Collectors.toList());
    }

    public static Collection<Text> getTrackedPlayers(UUID worldUUID) {
        return getTrackedPlayers(ScoreboardsListener.getScoreboard(worldUUID));
    }

    public static Collection<Text> getTrackedPlayers(Scoreboard scoreboard) {
        return scoreboard.getScores()
                .stream()
                .map(Score::getName)
                .sorted(Comparator.comparing(Text::toPlain))
                .collect(Collectors.toList());
    }

    public static Collection<Criterion> getAllCriteria() {
        return Stream.concat(
                Sponge.getRegistry().getAllOf(Criterion.class).stream(),
                Sponge.getRegistry().getAllOf(Statistic.class)
                        .stream()
                        .map(Statistic::getCriterion)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
        )
                .collect(Collectors.toList());
    }
}
