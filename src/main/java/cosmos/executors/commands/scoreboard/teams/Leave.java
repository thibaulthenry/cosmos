package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
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

    public Leave() {
        super(
                true,
                CosmosParameters.TARGETS.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    final Optional<Team> optionalTeam = super.serviceProvider.scoreboards()
                            .scoreboardOrCreate(worldKey)
                            .memberTeam(target);

                    final boolean sourceIsNotTarget = !super.serviceProvider.validation().isSelf(src, target);

                    if (optionalTeam.isPresent()) {
                        final Team team = optionalTeam.get();
                        team.removeMember(target);
                        super.addSuccess();

                        return super.serviceProvider.message()
                                .getMessage(src, "success.scoreboard.teams.leave")
                                .replace("target", target)
                                .replace("team", team)
                                .condition("target", sourceIsNotTarget)
                                .condition("verb", sourceIsNotTarget)
                                .green()
                                .asText();
                    }

                    return super.serviceProvider.message()
                            .getMessage(src, "error.missing.team")
                            .replace("target", target)
                            .condition("target", sourceIsNotTarget)
                            .condition("verb", sourceIsNotTarget)
                            .red()
                            .asText();
                })
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.processing.header")
                .replace("number", contents.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, true);
    }

}
