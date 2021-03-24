package cosmos.services.perworld.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.Directories;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.data.serializable.impl.ScoreboardData;
import cosmos.registries.perworld.ScoreboardRegistry;
import cosmos.registries.serializer.impl.ScoreboardSerializer;
import cosmos.services.io.FinderService;
import cosmos.services.message.MessageService;
import cosmos.services.perworld.ScoreboardService;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ScoreboardServiceImpl implements ScoreboardService {

    private final FinderService finderService;
    private final MessageService messageService;
    private final ScoreboardRegistry scoreboardRegistry;
    private final ScoreboardSerializer scoreboardSerializer;

    @Inject
    public ScoreboardServiceImpl(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
        this.messageService = injector.getInstance(MessageService.class);
        this.scoreboardRegistry = injector.getInstance(ScoreboardRegistry.class);
        this.scoreboardSerializer = injector.getInstance(ScoreboardSerializer.class);
    }

    @Override
    public Optional<Integer> findExtremum(final CommandContext context, final Parameter.Key<Integer> integerKey, final boolean negativeBound) {
        return context.one(integerKey)
                .map(Optional::of)
                .orElse(context.one(CosmosKeys.WILDCARD).map(value -> negativeBound ? Integer.MIN_VALUE : Integer.MAX_VALUE));
    }

    @Override
    public Optional<Component> findComponent(final CommandContext context) {
        return context.one(CosmosKeys.TEXT_JSON)
                .map(Optional::of)
                .orElse(context.one(CosmosKeys.TEXT_AMPERSAND));
    }

    @Override
    public int extremum(final CommandContext context, final Parameter.Key<Integer> integerKey, final boolean negativeBound) throws CommandException {
        final Audience src = context.cause().audience();

        return this.findExtremum(context, integerKey, negativeBound)
                .orElseThrow(this.messageService.supplyError(src, "error.invalid.value", "param", integerKey));
    }

    @Override
    public boolean isTargetsParameterFilled(final CommandContext context) {
        return context.hasAny(CosmosKeys.ENTITIES) || context.hasAny(CosmosKeys.MANY_SCORE_HOLDER)
                || context.hasAny(CosmosKeys.TEXT_AMPERSAND) || context.hasAny(CosmosKeys.TEXT_JSON);
    }

    @Override
    public Set<Objective> objectives(final ResourceKey worldKey) {
        return this.scoreboardOrCreate(worldKey).objectives();
    }

    @Override
    public Scoreboard scoreboardOrCreate(final ResourceKey worldKey) {
        return this.scoreboardRegistry.find(worldKey)
                .map(Optional::of)
                .orElse(
                        this.finderService.findCosmosPath(Directories.SCOREBOARDS, worldKey)
                                .flatMap(this.scoreboardSerializer::deserialize)
                                .flatMap(ScoreboardData::collect)
                )
                .flatMap(scoreboard -> this.scoreboardRegistry.register(worldKey, scoreboard).map(CosmosRegistryEntry::value))
                .orElseGet(() -> {
                    final Scoreboard scoreboard = Scoreboard.builder().build();

                    if (!this.scoreboardRegistry.register(worldKey, scoreboard).isPresent()) {
                        Cosmos.logger().error("An unexpected error occurred while registering a new scoreboard for world " + worldKey);
                    }

                    return scoreboard;
                });
    }

    @Override
    public Scoreboard scoreboardOrCreate(final ServerWorld world) {
        return this.scoreboardOrCreate(world.key());
    }

    @Override
    public Collection<Component> scoreHolders(final ResourceKey worldKey) {
        return this.scoreboardOrCreate(worldKey)
                .scores()
                .stream()
                .map(Score::name)
                .sorted(Comparator.comparing(component -> PlainComponentSerializer.plain().serialize(component)))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Component> targets(final CommandContext context, final ResourceKey worldKey, final boolean returnSource) throws CommandException {
        return this.targetsOrSources(
                context, CosmosKeys.ENTITIES, CosmosKeys.MANY_SCORE_HOLDER,
                CosmosKeys.TEXT_AMPERSAND, CosmosKeys.TEXT_JSON, worldKey, returnSource
        );
    }

    private Collection<Component> targetsOrSources(final CommandContext context, final Parameter.Key<List<Entity>> entitiesKey,
                                                   final Parameter.Key<List<Component>> scoreHoldersKey, final Parameter.Key<Component> textAmpersandKey,
                                                   final Parameter.Key<Component> textJsonKey, final ResourceKey worldKey,
                                                   final boolean returnSource) throws CommandException {
        final Audience src = context.cause().audience();

        if (!this.isTargetsParameterFilled(context)) {
            return returnSource ? this.tryReturnSource(src) : this.scoreHolders(worldKey);
        }

        final Optional<List<Component>> optionalSingleInputs = context.one(textAmpersandKey)
                .map(Optional::of)
                .orElse(context.one(textJsonKey))
                .map(Collections::singletonList);

        // TODO https://github.com/SpongePowered/Sponge/pull/3286

        final Optional<List<Component>> optionalTargets = context.one(entitiesKey)
                .map(entities -> entities
                        .stream()
                        .map(entity -> entity instanceof Tamer ? ((Tamer) entity).name() : entity.uniqueId().toString())
                        .map(Component::text)
                        .collect(Collectors.<Component>toList())
                )
                .map(Optional::of)
                .orElse(context.one(scoreHoldersKey))
                .map(Optional::of)
                .orElse(optionalSingleInputs);

        if (!optionalTargets.isPresent() || optionalTargets.get().isEmpty()) {
            throw this.messageService.getError(src, "error.missing.score-holders.selector", "world", worldKey);
        }

        return optionalTargets.get();
    }

    @Override
    public Collection<Component> sources(final CommandContext context, final ResourceKey worldKey, final boolean returnSource) throws CommandException {
        final Parameter.Key<List<Entity>> entitiesKey = Parameter.key("sources", new TypeToken<List<Entity>>() {});
        final Parameter.Key<List<Component>> scoreHoldersKey = Parameter.key("source-score-holders", new TypeToken<List<Component>>() {});
        final Parameter.Key<Component> textAmpersandKey = Parameter.key("source-text-ampersand", new TypeToken<Component>() {});
        final Parameter.Key<Component> textJsonKey = Parameter.key("source-text-json", new TypeToken<Component>() {});

        return this.targetsOrSources(context, entitiesKey, scoreHoldersKey, textAmpersandKey, textJsonKey, worldKey, returnSource);
    }

    @Override
    public Set<Team> teams(final ResourceKey worldKey) {
        return this.scoreboardOrCreate(worldKey).teams();
    }

    private List<Component> tryReturnSource(final Audience src) throws CommandException {
        if (src instanceof Tamer) {
            return Collections.singletonList(Component.text(((Tamer) src).name()));
        } else if (src instanceof Identifiable) {
            return Collections.singletonList(Component.text(((Identifiable) src).uniqueId().toString()));
        }

        throw this.messageService.getError(src, "error.missing.entities.any");
    }

}
