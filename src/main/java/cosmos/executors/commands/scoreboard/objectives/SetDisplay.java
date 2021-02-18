package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Optional;

@Singleton
public class SetDisplay extends AbstractScoreboardCommand {

    public SetDisplay() {
        super(
                Parameter.registryElement(TypeToken.get(DisplaySlot.class), RegistryTypes.DISPLAY_SLOT, ResourceKey.SPONGE_NAMESPACE)
                        .key(CosmosKeys.DISPLAY_SLOT)
                        .build(),
                CosmosParameters.OBJECTIVE_ALL.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final DisplaySlot displaySlot = context.one(CosmosKeys.DISPLAY_SLOT)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.DISPLAY_SLOT));

        final Optional<Objective> optionalObjective = context.one(CosmosKeys.OBJECTIVE);

        if (optionalObjective.isPresent()) {
            final Objective objective = optionalObjective.get();
            scoreboard.updateDisplaySlot(objective, displaySlot);

            super.serviceProvider.message()
                    .getMessage(src, "success.scoreboard.objectives.set-display")
                    .replace("obj", objective)
                    .replace("slot", displaySlot.key(RegistryTypes.DISPLAY_SLOT))
                    .replace("world", worldKey)
                    .green()
                    .sendTo(src);
        } else {
            scoreboard.objective(displaySlot)
                    .orElseThrow(super.serviceProvider.message().supplyError(src, "error.scoreboard.objectives.set-display.empty", "world", worldKey));

            scoreboard.clearSlot(displaySlot);

            super.serviceProvider.message()
                    .getMessage(src, "success.scoreboard.objectives.set-display.clear")
                    .replace("slot", displaySlot.key(RegistryTypes.DISPLAY_SLOT))
                    .replace("world", worldKey)
                    .green()
                    .sendTo(src);
        }
    }

}
