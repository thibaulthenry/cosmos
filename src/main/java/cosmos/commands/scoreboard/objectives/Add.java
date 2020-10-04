package cosmos.commands.scoreboard.objectives;

import cosmos.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.Units;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import cosmos.statics.arguments.TextArguments;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class Add extends AbstractScoreboardCommand {

    public Add() {
        super(
                GenericArguments.string(ArgKeys.OBJECTIVE_NAME.t),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.criterionChoices(ArgKeys.CRITERION)
                ),
                GenericArguments.optional(
                        TextArguments.allTexts(ArgKeys.DISPLAY_NAME)
                )
        );
    }

    @Override
    protected void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException {
        String objectiveName = args.<String>getOne(ArgKeys.OBJECTIVE_NAME.t).orElseThrow(Outputs.INVALID_OBJECTIVE.asSupplier());

        if (Validator.doesOverflowMaxLength(objectiveName, Units.NAME_MAX_LENGTH)) {
            throw Outputs.TOO_LONG_OBJECTIVE_NAME.asException(objectiveName);
        }

        if (scoreboard.getObjective(objectiveName).isPresent()) {
            throw Outputs.EXISTING_OBJECTIVE.asException(objectiveName);
        }

        Criterion criterion = args.<Criterion>getOne(ArgKeys.CRITERION.t).orElseThrow(Outputs.INVALID_CRITERION_CHOICE.asSupplier());

        Objective.Builder objectiveBuilder = Objective.builder()
                .name(objectiveName)
                .criterion(criterion);

        Optional<Text> optionalDisplayName = args.getOne(ArgKeys.DISPLAY_NAME.t);

        if (optionalDisplayName.isPresent()) {
            Text displayName = optionalDisplayName.get();

            if (Validator.doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH)) {
                throw Outputs.TOO_LONG_OBJECTIVE_DISPLAY_NAME.asException(displayName);
            }

            objectiveBuilder.displayName(displayName);
        }

        Objective objective = objectiveBuilder.build();

        scoreboard.addObjective(objectiveBuilder.build());

        src.sendMessage(Outputs.ADD_OBJECTIVE.asText(objective, worldName));
    }
}
