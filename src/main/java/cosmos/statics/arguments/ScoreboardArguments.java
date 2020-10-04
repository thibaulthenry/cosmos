package cosmos.statics.arguments;

import cosmos.constants.ArgKeys;
import cosmos.constants.ModifyCommands;
import cosmos.constants.TeamOptions;
import cosmos.statics.arguments.implementations.ExtremumElement;
import cosmos.statics.arguments.implementations.scoreboard.CriterionChoiceElement;
import cosmos.statics.arguments.implementations.scoreboard.DisplaySlotChoiceElement;
import cosmos.statics.arguments.implementations.scoreboard.ModifyCommandValueElement;
import cosmos.statics.arguments.implementations.scoreboard.ObjectiveChoiceElement;
import cosmos.statics.arguments.implementations.scoreboard.TargetChoiceElement;
import cosmos.statics.arguments.implementations.scoreboard.TeamChoiceElement;
import cosmos.statics.arguments.implementations.scoreboard.TeamOptionValueChoiceElement;
import cosmos.statics.finders.FinderScoreboard;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scoreboard.critieria.Criterion;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO SpongePowered/SpongeAPI Issue #2254

public class ScoreboardArguments {

    public static CommandElement objectiveChoices(ArgKeys argName, ArgKeys worldKey, Criterion... criteria) {
        return new ObjectiveChoiceElement(argName.t, worldKey.t, criteria);
    }

    private static CommandElement targetChoices(ArgKeys argName, ArgKeys worldKey) {
        return new TargetChoiceElement(argName.t, worldKey.t);
    }

    public static CommandElement targetOrEntityOrText(ArgKeys argName, ArgKeys worldKey, boolean returnSource) {
        return GenericArguments.seq(
                Arguments.firstParsing(
                        argName,
                        returnSource,
                        TextArguments.playerNames(argName, worldKey, returnSource),
                        targetChoices(argName, worldKey),
                        TextArguments.allTexts(argName)
                ),
                GenericArguments.markTrue(ArgKeys.IS_FILLED.t)
        );
    }

    public static CommandElement targetOrEntityOrText(ArgKeys argName, ArgKeys worldKey) {
        return targetOrEntityOrText(argName, worldKey, false);
    }

    public static CommandElement displaySlotChoices(ArgKeys argName) {
        return new DisplaySlotChoiceElement(argName.t);
    }

    public static CommandElement teamChoices(ArgKeys argName, ArgKeys worldKey) {
        return new TeamChoiceElement(argName.t, worldKey.t);
    }

    public static CommandElement teamOptionChoices(ArgKeys argName) {
        return ChoicesArguments.choices(
                argName,
                () -> toTeamOptionsMap(FinderScoreboard.getTeamOptions()),
                true
        );
    }

    public static CommandElement teamOptionValueChoices(ArgKeys argName, ArgKeys teamOptionValueKey) {
        return new TeamOptionValueChoiceElement(argName.t, teamOptionValueKey.t);
    }

    public static CommandElement modifyCommandChoices(ArgKeys argName) {
        return ChoicesArguments.choices(
                argName,
                () -> toModifyCommandsMap(FinderScoreboard.getModifyCommands()),
                true
        );
    }

    public static CommandElement modifyCommandValue(ArgKeys argName, ArgKeys modifyCommandKey) {
        return new ModifyCommandValueElement(argName, modifyCommandKey);
    }

    public static CommandElement criterionChoices(ArgKeys argName) {
        return new CriterionChoiceElement(argName.t);
    }

    public static CommandElement extremum(ArgKeys argName, boolean positive) {
        return new ExtremumElement(argName.t, positive);
    }

    private static Map<String, ModifyCommands> toModifyCommandsMap(Collection<ModifyCommands> collection) {
        return collection
                .stream()
                .distinct()
                .collect(Collectors.toMap(ModifyCommands::getKey, Function.identity()));
    }

    private static Map<String, TeamOptions> toTeamOptionsMap(Collection<TeamOptions> collection) {
        return collection
                .stream()
                .distinct()
                .collect(Collectors.toMap(TeamOptions::getKey, Function.identity()));
    }
}
