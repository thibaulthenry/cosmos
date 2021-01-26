package cosmos.executors.commands.scoreboard.teams.modify;

import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Units;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;

@Singleton
public class Prefix extends AbstractTeamModifyCommand {

    @Inject
    public Prefix() {
        super(CosmosParameters.TEXTS_ALL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard, final Team team) throws CommandException {
        final Component prefix = super.serviceProvider.perWorld().scoreboards().findComponent(context)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.text"));

        if (super.serviceProvider.validation().doesOverflowMaxLength(prefix, Units.NAME_MAX_LENGTH)) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.objective.overflow")
                    .condition("display1", false)
                    .condition("display2", false)
                    .asError();
        }

        team.setPrefix(prefix);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.modify")
                .replace("prop", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName()))
                .replace("team", team)
                .replace("value", prefix)
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
