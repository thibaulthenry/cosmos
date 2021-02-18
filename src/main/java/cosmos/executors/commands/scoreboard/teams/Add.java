package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;

@Singleton
public class Add extends AbstractScoreboardCommand {

    public Add() {
        super(
                Parameter.string().key(CosmosKeys.NAME).build(),
                CosmosParameters.TEXTS_ALL.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        final String name = context.one(CosmosKeys.NAME)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        if (super.serviceProvider.validation().doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH)) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.team.overflow")
                    .replace("name", name)
                    .asError();
        }

        if (scoreboard.team(name).isPresent()) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.scoreboard.teams.add.already-existing")
                    .replace("name", name)
                    .replace("world", worldKey)
                    .asError();
        }

        final Team.Builder teamBuilder = Team.builder().name(name);
        super.serviceProvider.scoreboards().findComponent(context).ifPresent(teamBuilder::displayName);
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
