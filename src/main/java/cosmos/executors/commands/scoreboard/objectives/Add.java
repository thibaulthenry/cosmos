package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Singleton;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.criteria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Optional;

@Singleton
public class Add extends AbstractScoreboardCommand {

    public Add() {
        super(
                Parameter.string().setKey(CosmosKeys.NAME).build(),
                CosmosParameters.CRITERION,
                CosmosParameters.TEXTS_ALL_OPTIONAL // todo display name
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final String name = context.getOne(CosmosKeys.NAME)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.value")); // todo

        if (this.serviceProvider.validation().doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH)) {
            // todo throw Outputs.TOO_LONG_OBJECTIVE_NAME.asException(objectiveName);
        }

        if (scoreboard.getObjective(name).isPresent()) {
            // todo throw Outputs.EXISTING_OBJECTIVE.asException(objectiveName);
        }

        final Criterion criterion = context.getOne(CosmosKeys.CRITERION)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.value")); // todo

        final Objective.Builder objectiveBuilder = Objective.builder()
                .name(name)
                .criterion(criterion);

        final Optional<Component> optionalDisplayName = Optional.empty(); // todo context.getOne(CosmosParameters.TEXTS_ALL_OPTIONAL);

        if (optionalDisplayName.isPresent()) {
            final Component displayName = optionalDisplayName.get();

            if (this.serviceProvider.validation().doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH)) {
                // todo throw Outputs.TOO_LONG_OBJECTIVE_DISPLAY_NAME.asException(displayName);
            }

            objectiveBuilder.displayName(displayName);
        }

        final Objective objective = objectiveBuilder.build();

        scoreboard.addObjective(objectiveBuilder.build());

        // todo src.sendMessage(Outputs.ADD_OBJECTIVE.asText(objective, worldName));
    }
}
