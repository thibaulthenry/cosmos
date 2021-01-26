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
                CosmosParameters.CRITERION, // todo Missing criterion + dummy without namespace
                CosmosParameters.TEXTS_ALL_OPTIONAL
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final String name = context.getOne(CosmosKeys.NAME)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        if (super.serviceProvider.validation().doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH)) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.objective.overflow")
                    .replace("name", name)
                    .condition("display1", false)
                    .condition("display2", false)
                    .asError();
        }

        if (scoreboard.getObjective(name).isPresent()) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.scoreboard.objectives.add.already-existing")
                    .replace("name", name)
                    .replace("world", worldKey)
                    .asError();
        }

        final Criterion criterion = context.getOne(CosmosKeys.CRITERION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.CRITERION));

        final Objective.Builder objectiveBuilder = Objective.builder()
                .name(name)
                .criterion(criterion);

        final Optional<Component> optionalDisplayName = super.serviceProvider.perWorld().scoreboards().findComponent(context);

        if (optionalDisplayName.isPresent()) {
            final Component displayName = optionalDisplayName.get();

            if (super.serviceProvider.validation().doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH)) {
                throw super.serviceProvider.message()
                        .getMessage(src, "error.invalid.objective.overflow")
                        .condition("display1", true)
                        .condition("display2", true)
                        .asError();
            }

            objectiveBuilder.displayName(displayName);
        }

        final Objective objective = objectiveBuilder.build();

        scoreboard.addObjective(objectiveBuilder.build());

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.objectives.add")
                .replace("obj", objective)
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
