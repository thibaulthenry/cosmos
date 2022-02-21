package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.Units;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class Add extends AbstractMultiTargetCommand {

    public Add() {
        super(
                CosmosParameters.TARGETS.get().build(),
                CosmosParameters.OBJECTIVE_ALL.get().build(),
                Parameter.integerNumber().key(CosmosKeys.AMOUNT).build()
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

        final int amount = context.one(CosmosKeys.AMOUNT)
                .filter(value -> value > 0)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.number.ge", "param", CosmosKeys.AMOUNT));

        final Collection<Component> contents = targets
                .stream()
                .map(target -> {
                    if (super.serviceProvider.validation().doesOverflowMaxLength(target, Units.SCORE_HOLDER_MAX_LENGTH)) {
                        return super.serviceProvider.message()
                                .getMessage(src, "error.invalid.score-holder.overflow")
                                .replace("name", target)
                                .red()
                                .asText();
                    }

                    try {
                        final Score score = objective.findOrCreateScore(target);
                        final int result = Math.addExact(score.score(), amount);
                        score.setScore(result);
                        super.addSuccess();

                        return super.serviceProvider.message()
                                .getMessage(src, "success.scoreboard.players.change")
                                .replace("amount", amount)
                                .replace("obj", objective)
                                .replace("result", result)
                                .replace("target", target)
                                .condition("plus", true)
                                .green()
                                .asText();
                    } catch (final ArithmeticException ignored) {
                        return super.serviceProvider.message()
                                .getMessage(src, "error.result.overflow")
                                .replace("target", target)
                                .red()
                                .asText();
                    }
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
