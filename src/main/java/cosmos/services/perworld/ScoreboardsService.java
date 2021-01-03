package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.perworld.impl.ScoreboardsServiceImpl;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.Set;

@ImplementedBy(ScoreboardsServiceImpl.class)
public interface ScoreboardsService extends WorldRelatedPerWorldService {

    Set<Objective> getObjectives(ResourceKey worldKey);

    Set<Objective> getObjectives(Scoreboard scoreboard);

    Scoreboard getOrCreateScoreboard(ResourceKey worldKey);

    Scoreboard getOrCreateScoreboard(ServerWorld world);

    Collection<Scoreboard> getScoreboards();

    Set<Team> getTeams(ResourceKey worldKey);

    Set<Team> getTeams(Scoreboard scoreboard);

    Collection<Component> getTracked(ResourceKey worldKey); // todo rename ScoreHolders

    Collection<Component> getTracked(Scoreboard scoreboard); // todo rename ScoreHolders

}
