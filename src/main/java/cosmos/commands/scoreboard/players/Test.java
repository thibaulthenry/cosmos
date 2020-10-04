package cosmos.commands.scoreboard.players;

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
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class Test extends AbstractMultiTargetCommand {

    public Test() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                ),
                ScoreboardArguments.extremum(ArgKeys.MIN, false),
                GenericArguments.optional(
                        ScoreboardArguments.extremum(ArgKeys.MAX, true)
                )
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        int min = args.<Integer>getOne(ArgKeys.MIN.t).orElseThrow(Outputs.NOT_A_NUMBER.asSupplier("Min value"));
        int max = args.<Integer>getOne(ArgKeys.MAX.t).orElse(Integer.MAX_VALUE);

        if (min > max) {
            throw Outputs.INVALID_DIFFERENCE.asException("min", "max", 1);
        }

        Objective objective = args.<Objective>getOne(ArgKeys.OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        Collection<Text> contents = targets.stream()
                .map(target -> {
                    Optional<Score> optionalScore = objective.getScore(target);

                    if (!optionalScore.isPresent()) {
                        return Outputs.MISSING_TARGET_SCORE.asText(target, objective);
                    }

                    int score = optionalScore.get().getScore();

                    if (score >= min && score <= max) {
                        addSuccess();
                        return Outputs.TEST_SCORE.asText(score, target, min, max);
                    }

                    return Outputs.TESTING_SCORE.asText(score, target, min, max);
                })
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "testing", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
