package cosmos.executors.commands.scoreboard.objectives.modify;

import com.google.common.base.CaseFormat;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;

@Singleton
public class DisplayName extends AbstractObjectiveModifyCommand {

    public DisplayName() {
        super(CosmosParameters.TEXTS_ALL.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard, final Objective objective) throws CommandException {
        final Component displayName = super.serviceProvider.scoreboard().findComponent(context)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.text"));

        objective.setDisplayName(displayName);

        super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.objectives.modify")
                .replace("obj", objective)
                .replace("prop", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName()))
                .replace("value", displayName)
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
