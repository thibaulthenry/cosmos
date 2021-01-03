package cosmos.executors.commands.scoreboard.objectives;

import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class List extends AbstractScoreboardCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final ResourceKey worldKey, final Scoreboard scoreboard) throws CommandException {
        if (scoreboard.getObjectives().isEmpty()) {
            // todo throw Outputs.MISSING_OBJECTIVE.asException(worldName);
        }

        final Collection<Component> contents = scoreboard.getObjectives()
                .stream()
                .map(objective -> Component.empty()/* todo Outputs.SHOW_TRACKED_OBJECTIVE.asText(objective, objective.getDisplayName(), objective.getCriterion()) */)
                .collect(Collectors.toList());

        final TextComponent title = Component.empty(); // todo  Outputs.SHOW_ALL_TRACKED_OBJECTIVES.asText(objectives.size(), worldName);

        this.serviceProvider.pagination().send(src, title, contents, false);
    }

}
