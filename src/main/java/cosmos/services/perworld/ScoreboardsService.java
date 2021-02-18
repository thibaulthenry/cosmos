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
public interface ScoreboardsService {

    int extremum(CommandContext context, Parameter.Key<Integer> integerKey, boolean negativeBound) throws CommandException;

    Optional<Integer> findExtremum(CommandContext context, Parameter.Key<Integer> integerKey, boolean negativeBound);

    Optional<Component> findComponent(CommandContext context);

    boolean isTargetsParameterFilled(CommandContext context);

    Set<Objective> objectives(ResourceKey worldKey);

    Scoreboard scoreboardOrCreate(ResourceKey worldKey);

    Scoreboard scoreboardOrCreate(ServerWorld world);

    Collection<Component> scoreHolders(ResourceKey worldKey);

    Collection<Component> sources(CommandContext context, ResourceKey worldKey, boolean returnSource) throws CommandException;

    Collection<Component> targets(CommandContext context, ResourceKey worldKey, boolean returnSource) throws CommandException;

    Set<Team> teams(ResourceKey worldKey);

}
