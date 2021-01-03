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
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Singleton
public class Random extends AbstractMultiTargetCommand {

    @Inject
    public Random(final Injector injector) {
        super(
                injector.getInstance(Targets.class).get(),
                injector.getInstance(ObjectiveAll.class).builder().build(),
                Parameter.integerNumber().setKey(CosmosKeys.MIN).build(), // todo extremum
                Parameter.integerNumber().setKey(CosmosKeys.MAX).build() // todo extremum
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.INVALID_OBJECTIVE_CHOICE.asSupplier(worldName));

        final int min = context.getOne(CosmosKeys.MIN)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.NOT_A_NUMBER.asSupplier("Min value"));
        final int max = context.getOne(CosmosKeys.MAX)
                .orElseThrow(() -> new CommandException(Component.empty())); // todo .orElseThrow(Outputs.NOT_A_NUMBER.asSupplier("Max value"));

        if (min >= max) {
            throw new CommandException(Component.empty()); // todo Outputs.INVALID_DIFFERENCE.asException("min", "max", 0);
        }

        final Collection<Component> contents = targets.stream().map(target -> {
            if (this.serviceProvider.validation().doesOverflowMaxLength(target, Units.PLAYER_NAME_MAX_LENGTH)) {
                return Component.empty(); // todo return Outputs.TOO_LONG_PLAYER_NAME.asText(target);
            }

            final int random = ThreadLocalRandom.current().nextInt(min, max == Integer.MAX_VALUE ? max : max + 1);
            objective.getOrCreateScore(target).setScore(random);
            // todo addSuccess();

            return Component.empty(); // todo return Outputs.SET_RANDOM_SCORE.asText(random, target, objective);
        }).collect(Collectors.toList());


        final TextComponent title = Component.empty(); // todo  Outputs.SHOW_SCORE_OPERATIONS.asText(contents.size(), "random mutation(s)", worldName);

        this.serviceProvider.pagination().send(src, title, contents, true);
    }
}
