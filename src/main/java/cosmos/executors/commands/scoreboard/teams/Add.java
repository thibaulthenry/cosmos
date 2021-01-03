package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Singleton;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;

import java.util.Optional;

@Singleton
public class Add extends AbstractScoreboardCommand {

    public Add() {
        super(
                Parameter.string().setKey(CosmosKeys.NAME).build(),
                CosmosParameters.TEXTS_ALL_OPTIONAL
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final String name = context.getOne(CosmosKeys.NAME)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_TEAM.asSupplier());

        if (this.serviceProvider.validation().doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH)) {
            throw new CommandException(Component.empty()); // todo throw Outputs.TOO_LONG_TEAM_NAME.asException(name);
        }

        if (scoreboard.getTeam(name).isPresent()) {
            throw new CommandException(Component.empty()); // todo throw Outputs.EXISTING_TEAM.asException(name);
        }

        final Team.Builder teamBuilder = Team.builder()
                .name(name);
        final Optional<Component> optionalDisplayName = Optional.empty(); // todo context.getOne(CosmosKeys.DISPLAY_NAME.t);

        if (optionalDisplayName.isPresent()) {
            final Component displayName = optionalDisplayName.get();

            if (this.serviceProvider.validation().doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH)) {
                throw new CommandException(Component.empty()); // todo throw Outputs.TOO_LONG_TEAM_DISPLAY_NAME.asException(displayName);
            }

            teamBuilder.displayName(displayName);
        }

        final Team team = teamBuilder.build();
        scoreboard.registerTeam(team);

        // todo src.sendMessage(Outputs.ADD_TEAM.asText(team, worldName));
    }
}
