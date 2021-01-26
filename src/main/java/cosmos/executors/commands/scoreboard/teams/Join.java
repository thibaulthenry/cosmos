package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.Targets;
import cosmos.executors.parameters.impl.scoreboard.TeamAll;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Team;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class Join extends AbstractMultiTargetCommand {

    @Inject
    public Join(final Injector injector) {
        super(
                true,
                injector.getInstance(TeamAll.class).build(),
                injector.getInstance(Targets.class).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Team team = context.getOne(CosmosKeys.TEAM)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.team")
                                .replace("param", CosmosKeys.TEAM)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    if (super.serviceProvider.validation().doesOverflowMaxLength(target, Units.SCORE_HOLDER_MAX_LENGTH)) {
                        return super.serviceProvider.message()
                                .getMessage(src, "error.invalid.score-holder.overflow")
                                .replace("name", target)
                                .red()
                                .asText();
                    }

                    final boolean sourceIsNotTarget = !super.serviceProvider.validation().isSelf(src, target);
                    team.addMember(target);
                    super.success();

                    return super.serviceProvider.message()
                            .getMessage(src, "success.scoreboard.teams.join")
                            .replace("target", target)
                            .replace("team", team)
                            .condition("target", sourceIsNotTarget)
                            .condition("verb", sourceIsNotTarget)
                            .green()
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
