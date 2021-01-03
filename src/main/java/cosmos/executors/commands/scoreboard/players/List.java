package cosmos.executors.commands.scoreboard.players;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.impl.scoreboard.Targets;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Singleton
public class List extends AbstractMultiTargetCommand {

    @Inject
    public List(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get() // todo builder for optional
        );
    }

    private PaginationList getTrackedPlayers(final ResourceKey worldKey, final Collection<Component> targets) {
        final TextComponent title = Component.empty(); // todo Outputs.SHOW_TRACKED_PLAYERS.asText(targets.size(), worldName);
        // todo addSuccess(targets.size());
        return this.serviceProvider.pagination().generate(title, targets);
    }

    private PaginationList getTrackedScores(final ResourceKey worldKey, final Component target, final boolean nested) throws CommandException {
        final Collection<Score> targetScores = super.getScoreboard(worldKey).getScores(target);

        if (targetScores.isEmpty()) {
            throw new CommandException(Component.empty()); // todo throw Outputs.MISSING_TRACKED_SCORE.asException(target, worldName);
        }

        final TextComponent title = nested ?
                Component.empty() : // todo Outputs.SHOW_TRACKED_SCORES_NESTED.asText(targetScores.size(), target) :
                Component.empty(); // todo Outputs.SHOW_TRACKED_SCORES.asText(targetScores.size(), target, worldName);

        final Collection<Component> contents = targetScores
                .stream()
                .map(score -> score.getObjectives()
                        .stream()
                        .map(objective -> {
                            // todo addSuccess();
                            return Component.empty(); // todo  Outputs.SHOW_TRACKED_SCORE.asText(objective.getDisplayName(), score.getScore(), objective);
                        })
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return this.serviceProvider.pagination().generate(title, contents);
    }

    private Collection<Component> getFlatTrackedScores(final ResourceKey worldKey, final Component target) {
        try {
            final PaginationList paginationList = this.getTrackedScores(worldKey, target, true);
            return paginationList.getTitle()
                    .map(title -> {
                        final java.util.List<Component> targetContents = Lists.newArrayList(paginationList.getContents());
                        targetContents.add(0, title);
                        return targetContents;
                    })
                    .orElse(Collections.singletonList(Component.empty()/* todo Outputs.MISSING_TRACKED_SCORE.asText(target, worldName)*/));
        } catch (final CommandException e) {
            return Collections.singleton(e.componentMessage());
        }
    }

    private PaginationList getAllTrackedScores(final ResourceKey worldKey, final Collection<Component> targets) {
        final Collection<Component> contents = targets
                .stream()
                .map(target -> this.getFlatTrackedScores(worldKey, target))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_ALL_TRACKED_SCORES.asText(targets.size(), worldName);

        return this.serviceProvider.pagination().generate(title, contents);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final PaginationList paginationList;

        if (false/*!args.hasAny(ArgKeys.IS_FILLED.t)*/) { // todo
            paginationList = this.getTrackedPlayers(worldKey, targets);
        } else if (targets.size() == 1) {
            paginationList = this.getTrackedScores(worldKey, Iterables.get(targets, 0), false);
        } else {
            paginationList = this.getAllTrackedScores(worldKey, targets);
        }

        this.serviceProvider.pagination().send(src, paginationList, false);
    }
}
