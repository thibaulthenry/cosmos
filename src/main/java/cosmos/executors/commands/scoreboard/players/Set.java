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
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class Set extends AbstractMultiTargetCommand {

    public Set() {
        super(
                CosmosParameters.TARGETS.get().build(),
                CosmosParameters.OBJECTIVE_ALL.get().build(),
                Parameter.integerNumber().key(CosmosKeys.SCORE).build()
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

        final int score = context.one(CosmosKeys.SCORE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.SCORE));

        final Collection<Component> contents = targets.stream().map(target -> {
            if (super.serviceProvider.validation().doesOverflowMaxLength(target, Units.SCORE_HOLDER_MAX_LENGTH)) {
                return super.serviceProvider.message()
                        .getMessage(src, "error.invalid.score-holder.overflow")
                        .replace("name", target)
                        .red()
                        .asText();
            }

            objective.findOrCreateScore(target).setScore(score);
            super.addSuccess();

            return super.serviceProvider.message()
                    .getMessage(src, "success.scoreboard.players.set")
                    .replace("obj", objective)
                    .replace("score", score)
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
