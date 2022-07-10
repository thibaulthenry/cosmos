package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.perworld.impl.ScoreboardServiceImpl;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.Set;

@ImplementedBy(ScoreboardServiceImpl.class)
public interface ScoreboardService extends CosmosService {

    boolean isTargetsParameterFilled(CommandContext context);

    Set<Objective> objectives(ResourceKey worldKey);

    Scoreboard scoreboardOrCreate(ResourceKey worldKey);

    Scoreboard scoreboardOrCreate(ServerWorld world);

    Collection<Component> scoreHolders(ResourceKey worldKey);

    Collection<Component> sources(CommandContext context, ResourceKey worldKey, boolean returnSource) throws CommandException;

    Collection<Component> targets(CommandContext context, ResourceKey worldKey, boolean returnSource) throws CommandException;

    Set<Team> teams(ResourceKey worldKey);

}
