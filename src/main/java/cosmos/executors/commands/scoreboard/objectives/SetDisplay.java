package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.scoreboard.ObjectiveAll;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Optional;

@Singleton
public class SetDisplay extends AbstractScoreboardCommand {

    @Inject
    public SetDisplay(final Injector injector) {
        super(
                CosmosParameters.DISPLAY_SLOT,
                injector.getInstance(ObjectiveAll.class).builder().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final DisplaySlot displaySlot = context.getOne(CosmosKeys.DISPLAY_SLOT)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo Outputs.INVALID_DISPLAY_SLOT_CHOICE.asSupplier()

        final Optional<Objective> optionalObjective = context.getOne(CosmosKeys.OBJECTIVE);

        if (optionalObjective.isPresent()) {
            final Objective objective = optionalObjective.get();
            scoreboard.updateDisplaySlot(objective, displaySlot);
            // todo src.sendMessage(Outputs.SET_DISPLAY_SLOT.asText(objective, displaySlot, worldName));
        } else {
            scoreboard.getObjective(displaySlot);
            // todo .orElseThrow(Outputs.MISSING_DISPLAY_SLOT.asSupplier(worldName));
            scoreboard.clearSlot(displaySlot);
            // todo src.sendMessage(Outputs.CLEAR_DISPLAY_SLOT.asText(displaySlot, worldName));
        }
    }
}
