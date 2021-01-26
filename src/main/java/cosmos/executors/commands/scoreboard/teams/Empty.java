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
        super(injector.getInstance(TeamAll.class).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final Team team = context.getOne(CosmosKeys.TEAM)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.team")
                                .replace("param", CosmosKeys.TEAM)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final Collection<Component> members = team.getMembers();

        if (members.isEmpty()) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.scoreboard.teams.empty.already-empty")
                    .replace("team", team)
                    .replace("world", worldKey)
                    .asError();
        }

        members.forEach(team::removeMember);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.empty")
                .replace("number", members.size())
                .replace("team", team)
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
