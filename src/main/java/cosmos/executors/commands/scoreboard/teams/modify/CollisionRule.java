package cosmos.executors.commands.scoreboard.teams.modify;

import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;

@Singleton
public class CollisionRule extends AbstractTeamModifyCommand {

    @Inject
    public CollisionRule() {
        super(
                Parameter.registryElement(TypeToken.get(org.spongepowered.api.scoreboard.CollisionRule.class), RegistryTypes.COLLISION_RULE)
                        .setKey(CosmosKeys.COLLISION_RULE)
                        .build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard, final Team team) throws CommandException {
        final org.spongepowered.api.scoreboard.CollisionRule collisionRule = context.getOne(CosmosKeys.COLLISION_RULE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.COLLISION_RULE));

        team.setCollisionRule(collisionRule);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.teams.modify")
                .replace("prop", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName()))
                .replace("team", team)
                .replace("value", collisionRule.key(RegistryTypes.COLLISION_RULE))
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
