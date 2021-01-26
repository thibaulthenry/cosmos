package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.scoreboard.Extremum;
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Singleton
public class Random extends AbstractMultiTargetCommand {

    @Inject
    public Random(final Injector injector) {
        super(
                injector.getInstance(Targets.class).build(),
                injector.getInstance(ObjectiveAll.class).build(),
                injector.getInstance(Extremum.class).integerKey(CosmosKeys.MIN).build(),
                injector.getInstance(Extremum.class).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective objective = context.getOne(CosmosKeys.OBJECTIVE)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective")
                                .replace("param", CosmosKeys.OBJECTIVE)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final int min = super.serviceProvider.perWorld().scoreboards().getExtremum(context, CosmosKeys.MIN, true);
        final int max = super.serviceProvider.perWorld().scoreboards().getExtremum(context, CosmosKeys.MAX, false);

        if (min >= max) {
            throw super.serviceProvider.message().getError(src, "error.invalid.operation.range-difference", "value", 0);
        }

        final Collection<Component> contents = targets.stream().map(target -> {
            if (super.serviceProvider.validation().doesOverflowMaxLength(target, Units.SCORE_HOLDER_MAX_LENGTH)) {
                return super.serviceProvider.message()
                        .getMessage(src, "error.invalid.score-holder.overflow")
                        .replace("name", target)
                        .red()
                        .asText();
            }

            final int random = ThreadLocalRandom.current().nextInt(min, max == Integer.MAX_VALUE ? max : max + 1);
            objective.getOrCreateScore(target).setScore(random);
            super.success();

            return super.serviceProvider.message()
                    .getMessage(src, "success.scoreboard.players.random")
                    .replace("obj", objective)
                    .replace("score", random)
                    .replace("target", target)
                    .green()
                    .asText();
        }).collect(Collectors.toList());


        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.players.processing.header")
                .replace("number", contents.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, true);
    }

}
