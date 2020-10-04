package cosmos.commands.scoreboard.players;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.ScoreboardArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.stream.Collectors;

// TODO SpongePowered/SpongeAPI Issue #2230

public class Enable extends AbstractMultiTargetCommand {

    public Enable() {
        super(
                Arguments.limitCompleteElement(
                        ScoreboardArguments.targetOrEntityOrText(ArgKeys.TARGETS, ArgKeys.WORLD)
                ),
                Arguments.limitCompleteElement(
                        ScoreboardArguments.objectiveChoices(ArgKeys.OBJECTIVE_TRIGGER, ArgKeys.WORLD, Criteria.TRIGGER)
                )
        );
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        Objective objective = args.<Objective>getOne(ArgKeys.OBJECTIVE_TRIGGER.t)
                .orElseThrow(Outputs.INVALID_OBJECTIVE_WITH_CRITERION_CHOICE.asSupplier(worldName, Criteria.TRIGGER.getName()));

        Collection<Text> contents = targets
                .stream()
                .map(target -> {
                    // objective.getOrCreateScore(target).setLocked(false);
                    return Outputs.ENABLE_TRIGGER.asText(objective, target);
                })
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_TRIGGER_ACTIVATIONS.asText(contents.size(),  worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
