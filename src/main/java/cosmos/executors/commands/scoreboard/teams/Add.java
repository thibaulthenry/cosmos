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
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        if (super.serviceProvider.validation().doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH)) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.team.overflow")
                    .replace("name", name)
                    .condition("display1", false)
                    .condition("display2", false)
                    .asError();
        }

        if (scoreboard.getTeam(name).isPresent()) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.scoreboard.teams.add.already-existing")
                    .replace("name", name)
                    .replace("world", worldKey)
                    .asError();
        }

        final Team.Builder teamBuilder = Team.builder().name(name);
        final Optional<Component> optionalDisplayName = super.serviceProvider.perWorld().scoreboards().findComponent(context);

        if (optionalDisplayName.isPresent()) {
            final Component displayName = optionalDisplayName.get();

            if (super.serviceProvider.validation().doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH)) {
                throw super.serviceProvider.message()
                        .getMessage(src, "error.invalid.team.overflow")
                        .condition("display1", true)
                        .condition("display2", true)
                        .asError();
            }

            teamBuilder.displayName(displayName);
        }

        final Team team = teamBuilder.build();
        scoreboard.registerTeam(team);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.add")
                .replace("team", team)
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
