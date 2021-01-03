package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.ObjectiveTrigger;
import cosmos.executors.parameters.impl.scoreboard.Targets;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class Enable extends AbstractMultiTargetCommand {

    @Inject
    public Enable(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get(),
                injector.getInstance(ObjectiveTrigger.class).builder().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE) // todo objective trigger
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_OBJECTIVE_WITH_CRITERION_CHOICE.asSupplier(worldName, Criteria.TRIGGER.getName()));

        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    objective.getOrCreateScore(target).setLocked(false);
                    return Component.empty(); // todo return Outputs.ENABLE_TRIGGER.asText(objective, target);
                })
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_TRIGGER_ACTIVATIONS.asText(contents.size(),  worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
