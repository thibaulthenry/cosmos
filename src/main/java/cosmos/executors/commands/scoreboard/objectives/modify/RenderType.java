package cosmos.executors.commands.scoreboard.objectives.modify;

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
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;

@Singleton
public class RenderType extends AbstractObjectiveModifyCommand {

    public RenderType() {
        super(
                Parameter.registryElement(TypeToken.get(ObjectiveDisplayMode.class), RegistryTypes.OBJECTIVE_DISPLAY_MODE, ResourceKey.SPONGE_NAMESPACE)
                        .key(CosmosKeys.DISPLAY_MODE)
                        .build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard, final Objective objective) throws CommandException {
        final ObjectiveDisplayMode displayMode = context.one(CosmosKeys.DISPLAY_MODE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.DISPLAY_MODE));

        objective.setDisplayMode(displayMode);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.objectives.modify")
                .replace("obj", objective)
                .replace("prop", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName()))
                .replace("value", displayMode.key(RegistryTypes.OBJECTIVE_DISPLAY_MODE))
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
