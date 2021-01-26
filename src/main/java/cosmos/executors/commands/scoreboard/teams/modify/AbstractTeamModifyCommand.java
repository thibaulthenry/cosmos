package cosmos.executors.commands.scoreboard.teams.modify;

import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;

abstract class AbstractTeamModifyCommand extends AbstractScoreboardCommand {

    AbstractTeamModifyCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final Team team = context.getOne(CosmosKeys.TEAM)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.team")
                                .replace("param", CosmosKeys.TEAM)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        this.run(src, context, worldKey, scoreboard, team);
    }

    protected abstract void run(Audience src, CommandContext context, ResourceKey worldKey, Scoreboard scoreboard, Team team) throws CommandException;

}
