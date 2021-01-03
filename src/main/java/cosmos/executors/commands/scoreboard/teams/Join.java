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
                injector.getInstance(TeamAll.class).builder().build(),
                injector.getInstance(Targets.class).get() // todo return source
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Team team = context.getOne(CosmosKeys.TEAM)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_TEAM_CHOICE.asSupplier());

        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    if (this.serviceProvider.validation().doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                        return Component.empty(); // todo return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
                    }

                    team.addMember(target);
                    // todo addSuccess();

                    return Component.empty(); //  todo return Outputs.JOIN_TEAM.asText(target, team, worldName);
                })
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); //  todo Outputs.SHOW_TEAM_OPERATIONS.asText(contents.size(), "registration(s)", worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
