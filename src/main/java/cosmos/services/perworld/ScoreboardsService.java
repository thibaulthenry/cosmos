package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.perworld.impl.ScoreboardsServiceImpl;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@ImplementedBy(ScoreboardsServiceImpl.class)
public interface ScoreboardsService extends WorldRelatedPerWorldService {

    Optional<Integer> findExtremum(CommandContext context, Parameter.Key<Integer> integerKey, boolean negativeBound);

    Optional<Component> findComponent(CommandContext context);

    int getExtremum(CommandContext context, Parameter.Key<Integer> integerKey, boolean negativeBound) throws CommandException;

    Set<Objective> getObjectives(ResourceKey worldKey);

    Set<Objective> getObjectives(Scoreboard scoreboard);

    Scoreboard getOrCreateScoreboard(ResourceKey worldKey);

    Scoreboard getOrCreateScoreboard(ServerWorld world);

    Collection<Scoreboard> getScoreboards();

    Collection<Component> getTargets(CommandContext context, ResourceKey worldKey, boolean returnSource) throws CommandException;

    Set<Team> getTeams(ResourceKey worldKey);

    Set<Team> getTeams(Scoreboard scoreboard);

    Collection<Component> getScoreHolders(ResourceKey worldKey);

    Collection<Component> getScoreHolders(Scoreboard scoreboard);

    boolean isTargetsParameterFilled(CommandContext context);

}
