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
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Get extends AbstractMultiTargetCommand {

    @Inject
    public Get(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get(),
                injector.getInstance(ObjectiveAll.class).builder().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        final Collection<Component> contents = targets.stream().map(target -> {
            final Optional<Score> optionalScore = objective.getScore(target);

            return optionalScore
                    .map(score -> {
                        // todo addSuccess();
                        return Component.empty(); // todo return Outputs.GET_SCORE.asText(target, objective, score.getScore());
                    })
                    .orElse(Component.empty()); // todo .orElse(Outputs.MISSING_TARGET_SCORE.asText(target.toPlain(), objective));
        }).collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "retrieval(s)", worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
