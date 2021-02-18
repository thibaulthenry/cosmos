package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.registries.message.Message;
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
public class Test extends AbstractMultiTargetCommand {

    public Test() {
        super(
                CosmosParameters.TARGETS.get().build(),
                CosmosParameters.OBJECTIVE_ALL.get().build(),
                CosmosParameters.EXTREMUM.get().integerKey(CosmosKeys.MIN).build(),
                CosmosParameters.EXTREMUM.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Collection<Component> targets) throws CommandException {
        final Objective objective = context.one(CosmosKeys.OBJECTIVE)
                .orElseThrow(
                        super.serviceProvider.message()
                                .getMessage(src, "error.invalid.objective")
                                .replace("param", CosmosKeys.OBJECTIVE)
                                .replace("world", worldKey)
                                .asSupplier()
                );

        final int min = super.serviceProvider.scoreboards().extremum(context, CosmosKeys.MIN, true);

        final int max = super.serviceProvider.scoreboards()
                .findExtremum(context, CosmosKeys.MAX, false)
                .orElse(Integer.MAX_VALUE);

        if (min > max) {
            throw super.serviceProvider.message().getError(src, "error.invalid.operation.range-difference", "value", 1);
        }

        final Collection<Component> contents = targets.stream()
                .map(target -> {
                    final Optional<Score> optionalScore = objective.score(target);

                    if (!optionalScore.isPresent()) {
                        return super.serviceProvider.message()
                                .getMessage(src, "error.missing.score")
                                .replace("obj", objective)
                                .replace("target", target)
                                .condition("any", false)
                                .red()
                                .asText();
                    }

                    final int score = optionalScore.get().score();
                    final boolean inRange = score >= min && score <= max;

                    final Message message = super.serviceProvider.message()
                            .getMessage(src, "success.scoreboard.players.test")
                            .replace("max", max)
                            .replace("min", min)
                            .replace("obj", objective)
                            .replace("score", score)
                            .replace("target", target)
                            .condition("in", inRange)
                            .red();

                    if (inRange) {
                        message.green();
                        super.addSuccess();
                    }

                    return message.asText();
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
