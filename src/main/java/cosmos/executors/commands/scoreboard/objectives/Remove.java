package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.ObjectiveAll;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;

@Singleton
public class Remove extends AbstractScoreboardCommand {

    @Inject
    public Remove(final Injector injector) {
        super(injector.getInstance(ObjectiveAll.class).builder().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName)

        scoreboard.removeObjective(objective);

        // todo src.sendMessage(Outputs.REMOVE_OBJECTIVE.asText(objective, worldName));
    }
}
