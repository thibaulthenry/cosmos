package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.ObjectiveAll;
import cosmos.executors.parameters.impl.scoreboard.Targets;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class Set extends AbstractMultiTargetCommand {

    @Inject
    public Set(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get(),
                injector.getInstance(ObjectiveAll.class).builder().build(),
                Parameter.integerNumber().setKey(CosmosKeys.AMOUNT).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName)

        final int amount = context.getOne(CosmosKeys.AMOUNT)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo Outputs.INVALID_VALUE.asSupplier()

        final Collection<Component> contents = targets.stream().map(target -> {
            if (this.serviceProvider.validation().doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                return Component.empty(); // todo return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
            }

            objective.getOrCreateScore(target).setScore(amount);

            // todo addSuccess();
            return Component.empty(); // todo return Outputs.SET_SCORE.asText(score, target, objective);
        }).collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "mutation(s)", worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
