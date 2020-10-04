package cosmos.commands.scoreboard.players;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class List extends AbstractMultiTargetCommand {

    public List() {
        super(
                GenericArguments.optional(
                        Arguments.limitCompleteElement(
                                ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                        )
                )
        );
    }

    private PaginationList getTrackedPlayers(String worldName, Collection<Text> targets) {
        Text title = Outputs.SHOW_TRACKED_PLAYERS.asText(targets.size(), worldName);
        addSuccess(targets.size());
        return generatePaginationOutput(title, targets);
    }

    private PaginationList getTrackedScores(String worldName, Text target, boolean nested) throws CommandException {
        Collection<Score> targetScores = getScoreboard().getScores(target);

        if (targetScores.isEmpty()) {
            throw Outputs.MISSING_TRACKED_SCORE.asException(target, worldName);
        }

        Text title = nested ?
                Outputs.SHOW_TRACKED_SCORES_NESTED.asText(targetScores.size(), target) :
                Outputs.SHOW_TRACKED_SCORES.asText(targetScores.size(), target, worldName);

        Collection<Text> contents = targetScores
                .stream()
                .map(score -> score.getObjectives()
                        .stream()
                        .map(objective -> {
                            addSuccess();
                            return Outputs.SHOW_TRACKED_SCORE.asText(objective.getDisplayName(), score.getScore(), objective);
                        })
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return generatePaginationOutput(title, contents);
    }

    private Collection<Text> getFlatTrackedScores(String worldName, Text target) {
        try {
            PaginationList targetOutput = getTrackedScores(worldName, target, true);
            return targetOutput.getTitle()
                    .map(title -> {
                        java.util.List<Text> targetContents = Lists.newArrayList(targetOutput.getContents());
                        targetContents.add(0, title);
                        return targetContents;
                    })
                    .orElse(Collections.singletonList(Outputs.MISSING_TRACKED_SCORE.asText(target, worldName)));
        } catch (CommandException ce) {
            return Collections.singleton(ce.getText());
        }
    }

    private PaginationList getAllTrackedScores(String worldName, Collection<Text> targets) {
        Collection<Text> contents = targets
                .stream()
                .map(target -> getFlatTrackedScores(worldName, target))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_ALL_TRACKED_SCORES.asText(targets.size(), worldName);

        return generatePaginationOutput(title, contents);
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        PaginationList paginationList;

        if (!args.hasAny(ArgKeys.IS_FILLED.t)) {
            paginationList = getTrackedPlayers(worldName, targets);
        } else if (targets.size() == 1) {
            paginationList = getTrackedScores(worldName, Iterables.get(targets, 0), false);
        } else {
            paginationList = getAllTrackedScores(worldName, targets);
        }

        sendPaginatedOutput(src, paginationList, false);
    }
}
