package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.TeamAll;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class List extends AbstractScoreboardCommand {

    @Inject
    public List(final Injector injector) {
        super(injector.getInstance(TeamAll.class).builder().optional().build());
    }

    private PaginationList getRegisteredTeamsText(final ResourceKey worldKey) {
        final Collection<Component> contents = super.getScoreboard(worldKey).getTeams()
                .stream()
                .map(team -> Component.empty() /* todo Outputs.SHOW_TRACKED_TEAM.asText(team, team.getDisplayName(), team.getMembers().size())*/)
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_ALL_TRACKED_TEAMS.asText(contents.size(), worldName);

        return this.serviceProvider.pagination().generate(title, contents);
    }

    private PaginationList getTeamMembers(final ResourceKey worldKey, final Team team) {
        final Set<Component> contents = team.getMembers();
        final TextComponent title = Component.empty(); // todo Outputs.SHOW_TEAM_MEMBERS.asText(contents.size(), team, worldKey);
        return this.serviceProvider.pagination().generate(title, contents);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        if (this.getScoreboard(worldKey).getTeams().isEmpty()) {
            throw new CommandException(Component.empty()); // todo throw Outputs.MISSING_TEAM.asException(worldName);
        }

        final Optional<Team> optionalTeam = context.getOne(CosmosKeys.TEAM);

        final PaginationList paginationList = optionalTeam
                .map(team -> getTeamMembers(worldKey, team))
                .orElseGet(() -> getRegisteredTeamsText(worldKey));

        this.serviceProvider.pagination().send(src, paginationList, false);
    }

}
