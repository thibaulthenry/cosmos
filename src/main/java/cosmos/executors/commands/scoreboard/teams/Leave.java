package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.impl.scoreboard.Targets;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Team;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Leave extends AbstractMultiTargetCommand {

    @Inject
    public Leave(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get() // todo return source
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    final Optional<Team> optionalMemberTeam = super.getScoreboard(worldKey).getMemberTeam(target);

                    if (optionalMemberTeam.isPresent()) {
                        final Team memberTeam = optionalMemberTeam.get();
                        memberTeam.removeMember(target);
                        // todo addSuccess();

                        return Component.empty(); // todo return Outputs.LEAVE_TEAM.asText(target, memberTeam);
                    }

                    return Component.empty(); // todo return Outputs.MISSING_TARGET_TEAM.asText(target);
                })
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_TEAM_OPERATIONS.asText(contents.size(), "unsubscription(s)", worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
