package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
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
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Reset extends AbstractMultiTargetCommand {

    @Inject
    public Reset(final Injector injector) {
        super(
                injector.getInstance(Targets.class).build(),
                injector.getInstance(ObjectiveAll.class).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Optional<Objective> optionalObjective = context.getOne(CosmosKeys.OBJECTIVE);

        final Collection<Objective> objectives = optionalObjective
                .map(Collections::singleton)
                .orElse(super.serviceProvider.perWorld().scoreboards().getObjectives(worldKey));

        final Collection<Component> contents = targets
                .stream()
                .map(target -> objectives
                        .stream()
                        .filter(objective -> objective.hasScore(target))
                        .map(objective -> {
                            objective.removeScore(target);
                            super.success();

                            return super.serviceProvider.message()
                                    .getMessage(src, "success.scoreboard.players.reset")
                                    .replace("obj", objective)
                                    .replace("target", target)
                                    .green()
                                    .asText();
                        })
                        .collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
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
