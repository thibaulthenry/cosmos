package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;

@Singleton
public class Remove extends AbstractScoreboardCommand {

    public Remove() {
        super(CosmosParameters.OBJECTIVE_ALL.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final Objective objective = context.one(CosmosKeys.OBJECTIVE)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective")
                                .replace("param", CosmosKeys.OBJECTIVE)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        scoreboard.removeObjective(objective);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.objectives.remove")
                .replace("obj", objective)
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
