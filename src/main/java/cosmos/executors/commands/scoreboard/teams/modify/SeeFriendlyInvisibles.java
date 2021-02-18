package cosmos.executors.commands.scoreboard.teams.modify;

import com.google.common.base.CaseFormat;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;

@Singleton
public class SeeFriendlyInvisibles extends AbstractTeamModifyCommand {

    public SeeFriendlyInvisibles() {
        super(Parameter.bool().key(CosmosKeys.STATE).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard, final Team team) throws CommandException {
        final boolean state = context.one(CosmosKeys.STATE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.STATE));

        team.setCanSeeFriendlyInvisibles(state);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.modify.boolean")
                .replace("prop", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName()))
                .replace("team", team)
                .replace("world", worldKey)
                .condition("value", state)
                .green()
                .sendTo(src);
    }

}
