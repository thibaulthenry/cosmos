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
import net.kyori.adventure.text.format.NamedTextColor;
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
        super(injector.getInstance(TeamAll.class).optional().build());
    }

    private PaginationList getRegisteredTeamsText(final Audience src, final ResourceKey worldKey) {
        final Collection<Component> contents = super.serviceProvider.perWorld().scoreboards()
                .getOrCreateScoreboard(worldKey)
                .getTeams()
                .stream()
                .map(team -> super.serviceProvider.message()
                        .getMessage(src, "success.scoreboard.teams.list")
                        .replace("number", team.getMembers().size())
                        .replace("team", team)
                        .green()
                        .asText()
                )
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.list.header.teams")
                .replace("number", contents.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        return super.serviceProvider.pagination().generate(title, contents);
    }

    private PaginationList getTeamMembers(final Audience src, final ResourceKey worldKey, final Team team) {
        final Set<Component> contents = team.getMembers();

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.list.header.members")
                .replace("number", contents.size())
                .replace("team", team)
                .replace("world", worldKey)
                .gray()
                .asText();

        return super.serviceProvider.pagination().generate(title, contents);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        if (super.serviceProvider.perWorld().scoreboards().getOrCreateScoreboard(worldKey).getTeams().isEmpty()) {
            throw super.serviceProvider.message().getError(src, "error.scoreboard.teams.list.empty", "world", worldKey);
        }

        final Optional<Team> optionalTeam = context.getOne(CosmosKeys.TEAM);

        final PaginationList paginationList = optionalTeam
                .map(team -> this.getTeamMembers(src, worldKey, team))
                .orElse(this.getRegisteredTeamsText(src, worldKey));

        super.serviceProvider.pagination().send(src, paginationList, false);
    }

}
