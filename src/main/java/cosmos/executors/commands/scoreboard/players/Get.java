package cosmos.executors.commands.scoreboard.players;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.AbstractMultiTargetCommand;
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

    public Get() {
        super(
                CosmosParameters.TARGETS.get().build(),
                CosmosParameters.OBJECTIVE_ALL.get().build()
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

        final Collection<Component> contents = targets.stream()
                .map(target -> {
                    final Optional<Score> optionalScore = objective.score(target);

                    return optionalScore
                            .map(score -> {
                                super.addSuccess();

                                return super.serviceProvider.message()
                                        .getMessage(src, "success.scoreboard.players.get")
                                        .replace("obj", objective)
                                        .replace("score", score.score())
                                        .replace("target", target)
                                        .green()
                                        .asText();
                            })
                            .orElse(
                                    super.serviceProvider.message()
                                            .getMessage(src, "error.missing.score")
                                            .replace("obj", objective)
                                            .replace("target", target)
                                            .condition("any", false)
                                            .red()
                                            .asText()
                            );
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
