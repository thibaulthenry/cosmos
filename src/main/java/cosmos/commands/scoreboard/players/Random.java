package cosmos.commands.scoreboard.players;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.Units;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Random extends AbstractMultiTargetCommand {

    public Random() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE, ArgKeys.WORLD)
                ),
                ScoreboardArguments.extremum(ArgKeys.MIN, false),
                ScoreboardArguments.extremum(ArgKeys.MAX, true)
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Objective objective = args.<Objective>getOne(ArgKeys.OBJECTIVE.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        int min = args.<Integer>getOne(ArgKeys.MIN.t).orElseThrow(Outputs.NOT_A_NUMBER.asSupplier("Min value"));
        int max = args.<Integer>getOne(ArgKeys.MAX.t).orElseThrow(Outputs.NOT_A_NUMBER.asSupplier("Max value"));

        if (min >= max) {
            throw Outputs.INVALID_DIFFERENCE.asException("min", "max", 0);
        }

        Collection<Text> contents = targets.stream().map(target -> {
            if (Validator.doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
            }

            int random = ThreadLocalRandom.current().nextInt(min, max == Integer.MAX_VALUE ? max : max + 1);

            objective.getOrCreateScore(target).setScore(random);

            addSuccess();
            return Outputs.SET_RANDOM_SCORE.asText(random, target, objective);
        }).collect(Collectors.toList());


        Text title = Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "random mutation(s)", worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
