package cosmos.executors.commands.scoreboard.players;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
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

    public List() {
        super(CosmosParameters.TARGETS.get().optional().build());
    }

    private PaginationList getAllScores(final Audience src, final ResourceKey worldKey, final Collection<Component> targets) {
        final Collection<Component> contents = targets
                .stream()
                .map(target -> this.getFlatScores(src, worldKey, target))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.players.list.header.all-scores")
                .replace("number", targets.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        return super.serviceProvider.pagination().generate(title, contents);
    }

    private Collection<Component> getFlatScores(final Audience src, final ResourceKey worldKey, final Component target) {
        try {
            final PaginationList paginationList = this.getScores(src, worldKey, target, true);
            return paginationList.title()
                    .map(title -> {
                        final java.util.List<Component> targetContents = Lists.newArrayList(paginationList.contents());
                        targetContents.add(0, title);

                        return targetContents;
                    })
                    .orElse(
                            super.serviceProvider.message()
                                    .getMessage(src, "error.missing.score")
                                    .replace("target", target)
                                    .condition("any", true)
                                    .condition("obj", false)
                                    .red()
                                    .asSingleton()
                    );
        } catch (final CommandException e) {
            return Collections.singleton(e.componentMessage());
        }
    }

    private PaginationList getScoreHolders(final Audience src, final ResourceKey worldKey) throws CommandException {
        final Collection<Component> targets = super.serviceProvider.scoreboard().scoreHolders(worldKey);

        if (targets.isEmpty()) {
            throw super.serviceProvider.message().getError(src, "error.missing.score-holders", "world", worldKey);
        }

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.players.list.header.score-holders")
                .replace("number", targets.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        super.addSuccess(targets.size());

        return super.serviceProvider.pagination().generate(title, targets);
    }

    private PaginationList getScores(final Audience src, final ResourceKey worldKey, final Component target, final boolean nested) throws CommandException {
        final Collection<Score> targetScores = super.serviceProvider.scoreboard()
                .scoreboardOrCreate(worldKey)
                .scores(target);

        if (targetScores.isEmpty()) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.missing.score")
                    .replace("target", target)
                    .condition("any", true)
                    .condition("obj", false)
                    .asError();
        }

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.players.list.header.scores")
                .replace("number", targetScores.size())
                .replace("target", target)
                .replace("world", worldKey)
                .condition("world", !nested)
                .gray()
                .asText();

        final Collection<Component> contents = targetScores
                .stream()
                .map(score -> score.objectives()
                        .stream()
                        .map(objective -> {
                            super.addSuccess();

                            return super.serviceProvider.message()
                                    .getMessage(src, "success.scoreboard.players.list")
                                    .replace("obj", objective)
                                    .replace("score", score.score())
                                    .green()
                                    .asText();
                        })
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return super.serviceProvider.pagination().generate(title, contents);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final PaginationList paginationList;

        if (!super.serviceProvider.scoreboard().isTargetsParameterFilled(context)) {
            paginationList = this.getScoreHolders(src, worldKey);
        } else if (targets.size() == 1) {
            paginationList = this.getScores(src, worldKey, Iterables.get(targets, 0), false);
        } else {
            paginationList = this.getAllScores(src, worldKey, targets);
        }

        super.serviceProvider.pagination().send(src, paginationList, false);
    }

}
