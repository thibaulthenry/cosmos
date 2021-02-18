package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.criteria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;

@Singleton
public class Add extends AbstractScoreboardCommand {

    public Add() {
        super(
                Parameter.string().key(CosmosKeys.NAME).build(),
                // TODO https://github.com/SpongePowered/Sponge/issues/3274 + Not formatted like Vanilla
                Parameter.registryElement(TypeToken.get(Criterion.class), RegistryTypes.CRITERION, ResourceKey.SPONGE_NAMESPACE)
                        .key(CosmosKeys.CRITERION)
                        .build(),
                CosmosParameters.TEXTS_ALL.get()
                        .optional()
                        .build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final String name = context.one(CosmosKeys.NAME)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        if (super.serviceProvider.validation().doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH)) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.objective.overflow")
                    .replace("name", name)
                    .asError();
        }

        if (scoreboard.objective(name).isPresent()) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.scoreboard.objectives.add.already-existing")
                    .replace("name", name)
                    .replace("world", worldKey)
                    .asError();
        }

        final Criterion criterion = context.one(CosmosKeys.CRITERION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.CRITERION));

        final Objective.Builder objectiveBuilder = Objective.builder().name(name).criterion(criterion);
        super.serviceProvider.scoreboards().findComponent(context).ifPresent(objectiveBuilder::displayName);
        final Objective objective = objectiveBuilder.build();
        scoreboard.addObjective(objective);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.objectives.add")
                .replace("obj", objective)
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
