package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.impl.scoreboard.ObjectiveTrigger;
import cosmos.executors.parameters.impl.scoreboard.Targets;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.criteria.Criteria;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class Enable extends AbstractMultiTargetCommand {

    @Inject
    public Enable(final Injector injector) {
        super(
                injector.getInstance(Targets.class).build(),
                injector.getInstance(ObjectiveTrigger.class).key("trigger-objective").build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Parameter.Key<Objective> triggerObjectiveKey = Parameter.key("trigger-objective", Objective.class);

        final Objective objective = context.getOne(triggerObjectiveKey)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective.type")
                                .replace("criterion", Criteria.TRIGGER.get().key(RegistryTypes.CRITERION))
                                .replace("param", triggerObjectiveKey)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    objective.getOrCreateScore(target).setLocked(false);
                    super.success();

                    return super.serviceProvider.message()
                            .getMessage(src, "success.scoreboard.players.enable")
                            .replace("obj", objective)
                            .replace("target", target)
                            .green()
                            .asText();
                })
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.players.processing.header")
                .replace("number", contents.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, true);
    }

}
