package cosmos.services.perworld.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.perworld.ScoreboardsRegistry;
import cosmos.services.perworld.ScoreboardsService;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.world.server.ServerWorld;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ScoreboardsServiceImpl implements ScoreboardsService {

    private final ScoreboardsRegistry scoreboardsRegistry;

    @Inject
    public ScoreboardsServiceImpl(final ScoreboardsRegistry scoreboardsRegistry) {
        this.scoreboardsRegistry = scoreboardsRegistry;
    }

    @Override
    public Set<Objective> getObjectives(final ResourceKey worldKey) {
        return this.getObjectives(this.getOrCreateScoreboard(worldKey));
    }

    @Override
    public Set<Objective> getObjectives(final Scoreboard scoreboard) {
        return scoreboard.getObjectives();
    }

    @Override
    public Scoreboard getOrCreateScoreboard(final ResourceKey worldKey) {
        return this.scoreboardsRegistry.getOrCreate(worldKey);
    }

    @Override
    public Scoreboard getOrCreateScoreboard(final ServerWorld world) {
        return this.getOrCreateScoreboard(world.getKey());
    }

    @Override
    public Optional<Path> getPath(final ResourceKey worldKey) {
        return Optional.empty();
    }

    @Override
    public Optional<Path> getPath(final ServerWorld world) {
        return Optional.empty();
    }

    @Override
    public Collection<Scoreboard> getScoreboards() {
        return this.scoreboardsRegistry.values();
    }

    @Override
    public Set<Team> getTeams(final ResourceKey worldKey) {
        return this.getTeams(this.getOrCreateScoreboard(worldKey));
    }

    @Override
    public Set<Team> getTeams(final Scoreboard scoreboard) {
        return scoreboard.getTeams();
    }

    public Collection<Component> getTracked(final ResourceKey worldKey) {
        return this.getTracked(this.getOrCreateScoreboard(worldKey));
    }

    public Collection<Component> getTracked(final Scoreboard scoreboard) {
        return scoreboard.getScores()
                .stream()
                .map(Score::getName)
                // todo
                //  .sorted(Comparator.comparing(Text::toPlain))
                .collect(Collectors.toList());
    }

}
