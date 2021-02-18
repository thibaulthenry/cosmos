package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class List extends AbstractScoreboardCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        if (scoreboard.objectives().isEmpty()) {
            throw super.serviceProvider.message().getError(src, "error.scoreboard.objectives.list.empty", "world", worldKey);
        }

        final Collection<Component> contents = scoreboard.objectives()
                .stream()
                .map(objective -> super.serviceProvider.message()
                        .getMessage(src, "success.scoreboard.objectives.list")
                        .replace("criterion", objective.criterion().key(RegistryTypes.CRITERION))
                        .replace("display", objective.displayName())
                        .replace("obj", objective)
                        .green()
                        .asText()
                )
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.scoreboard.objectives.list.header")
                .replace("number", contents.size())
                .replace("world", worldKey)
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, false);
    }

}
