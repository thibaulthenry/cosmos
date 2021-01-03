package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.TeamAll;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;

import java.util.Collection;

@Singleton
public class Empty extends AbstractScoreboardCommand {

    @Inject
    public Empty(final Injector injector) {
        super(injector.getInstance(TeamAll.class).builder().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final Team team = context.getOne(CosmosKeys.TEAM)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_TEAM_CHOICE.asSupplier());

        final Collection<Component> members = team.getMembers();

        if (members.isEmpty()) {
            throw new CommandException(Component.empty()); // todo throw Outputs.EMPTY_TEAM.asException(team);
        }

        members.forEach(team::removeMember);

        // todo src.sendMessage(Outputs.REMOVE_ALL_PLAYERS_FROM_TEAM.asText(members.size(), team, worldName));
    }

}
