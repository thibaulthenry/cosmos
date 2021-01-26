package cosmos.executors.commands.scoreboard.objectives.modify;

import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.managed.clientcompletion.ClientCompletionTypes;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;

@Singleton
public class RenderType extends AbstractObjectiveModifyCommand {

    @Inject
    public RenderType() {
        super(CosmosParameters.DISPLAY_MODE);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard, final Objective objective) throws CommandException {
        final ObjectiveDisplayMode displayMode = context.getOne(CosmosKeys.DISPLAY_MODE)
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
