package cosmos.services.perworld.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.registries.data.serializable.impl.ScoreboardData;
import cosmos.registries.perworld.ScoreboardsRegistry;
import cosmos.registries.serializer.impl.ScoreboardsSerializer;
import cosmos.services.io.FinderService;
import cosmos.services.message.MessageService;
import cosmos.services.perworld.ScoreboardsService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.server.ServerWorld;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ScoreboardsServiceImpl implements ScoreboardsService {

    private final FinderService finderService;
    private final MessageService messageService;
    private final ScoreboardsRegistry scoreboardsRegistry;
    private final ScoreboardsSerializer scoreboardsSerializer;

    @Inject
    public ScoreboardsServiceImpl(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.messageService = injector.getInstance(MessageService.class);
        this.scoreboardsRegistry = injector.getInstance(ScoreboardsRegistry.class);
        this.scoreboardsSerializer = injector.getInstance(ScoreboardsSerializer.class);
    }

    @Override
    public Optional<Integer> findExtremum(final CommandContext context, final Parameter.Key<Integer> integerKey, final boolean negativeBound) {
        return context.getOne(integerKey)
                .map(Optional::of)
                .orElse(context.getOne(CosmosKeys.WILDCARD).map(value -> negativeBound ? Integer.MIN_VALUE : Integer.MAX_VALUE));
    }

    @Override
    public Optional<Component> findComponent(final CommandContext context) {
        return context.getOne(CosmosKeys.TEXT_JSON)
                .map(Optional::of)
                .orElse(context.getOne(CosmosKeys.TEXT_AMPERSAND));
    }

    @Override
    public int getExtremum(final CommandContext context, final Parameter.Key<Integer> integerKey, final boolean negativeBound) throws CommandException {
        final Audience src = context.getCause().getAudience();

        return this.findExtremum(context, integerKey, negativeBound)
                .orElseThrow(this.messageService.supplyError(src, "error.invalid.value", "param", integerKey));
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
        return this.scoreboardsRegistry.computeIfAbsent(worldKey, key -> this.getPath(worldKey)
                .flatMap(this.scoreboardsSerializer::deserialize)
                .flatMap(ScoreboardData::collect)
                .orElse(Scoreboard.builder().build())
        );
    }

    @Override
    public Scoreboard getOrCreateScoreboard(final ServerWorld world) {
        return this.getOrCreateScoreboard(world.getKey());
    }

    @Override
    public Optional<Path> getPath(final ResourceKey worldKey) {
        final String fileName = worldKey.getFormatted().replaceAll(":", "_") + ".dat";
        return this.finderService.getCosmosPath(Directories.SCOREBOARDS_DIRECTORY_NAME, fileName);
    }

    @Override
    public Optional<Path> getPath(final ServerWorld world) {
        return this.getPath(world.getKey());
    }

    @Override
    public Collection<Scoreboard> getScoreboards() {
        return this.scoreboardsRegistry.values();
    }

    @Override
    public Collection<Component> getTargets(final CommandContext context, final ResourceKey worldKey, final boolean returnSource) throws CommandException {
        final Audience src = context.getCause().getAudience();

        if (!this.isTargetsParameterFilled(context)) {
            return returnSource ? this.tryReturnSource(src) : this.getScoreHolders(worldKey);
        }

        final Optional<List<Component>> optionalSingleInputs = context.getOne(CosmosKeys.TEXT_AMPERSAND)
                .map(Optional::of)
                .orElse(context.getOne(CosmosKeys.TEXT_JSON))
                .map(Collections::singletonList);

        final Optional<List<Component>> optionalTargets = context.getOne(CosmosKeys.ENTITY_TARGETS) // todo entity is catching text
                .map(entities -> entities
                        .stream()
                        .map(entity -> entity instanceof Tamer ? ((Tamer) entity).getName() : entity.getUniqueId().toString())
                        .map(Component::text)
                        .collect(Collectors.<Component>toList())
                )
                .map(Optional::of)
                .orElse(context.getOne(CosmosKeys.MANY_SCORE_HOLDER))
                .map(Optional::of)
                .orElse(optionalSingleInputs);

        if (!optionalTargets.isPresent() || optionalTargets.get().isEmpty()) {
            throw this.messageService.getError(src, "error.missing.score-holders.selector", "world", worldKey);
        }

        return optionalTargets.get();
    }

    @Override
    public Set<Team> getTeams(final ResourceKey worldKey) {
        return this.getTeams(this.getOrCreateScoreboard(worldKey));
    }

    @Override
    public Set<Team> getTeams(final Scoreboard scoreboard) {
        return scoreboard.getTeams();
    }

    @Override
    public Collection<Component> getScoreHolders(final ResourceKey worldKey) {
        return this.getScoreHolders(this.getOrCreateScoreboard(worldKey));
    }

    @Override
    public Collection<Component> getScoreHolders(final Scoreboard scoreboard) {
        return scoreboard.getScores()
                .stream()
                .map(Score::getName)
                .sorted(Comparator.comparing(component -> PlainComponentSerializer.plain().serialize(component)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTargetsParameterFilled(final CommandContext context) {
        return context.hasAny(CosmosKeys.ENTITY_TARGETS) || context.hasAny(CosmosKeys.MANY_SCORE_HOLDER)
                || context.hasAny(CosmosKeys.TEXT_AMPERSAND) || context.hasAny(CosmosKeys.TEXT_JSON);
    }

    private List<Component> tryReturnSource(final Audience src) throws CommandException {
        if (src instanceof Tamer) {
            return Collections.singletonList(Component.text(((Tamer) src).getName()));
        } else if (src instanceof Identifiable) {
            return Collections.singletonList(Component.text(((Identifiable) src).getUniqueId().toString()));
        }

        throw this.messageService.getError(src, "error.missing.entities");
    }

}
