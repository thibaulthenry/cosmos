package cosmos.executors.commands.scoreboard.teams.modify;

import com.google.common.base.CaseFormat;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibility;

@Singleton
public class DeathMessageVisibility extends AbstractTeamModifyCommand {

    public DeathMessageVisibility() {
        super(
                Parameter.registryElement(TypeToken.get(Visibility.class), RegistryTypes.VISIBILITY, ResourceKey.SPONGE_NAMESPACE)
                        .key(CosmosKeys.VISIBILITY)
                        .build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard, final Team team) throws CommandException {
        final Visibility visibility = context.one(CosmosKeys.VISIBILITY)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.VISIBILITY));

        team.setDeathMessageVisibility(visibility);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.modify")
                .replace("prop", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName()))
                .replace("team", team)
                .replace("value", visibility.key(RegistryTypes.VISIBILITY))
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}