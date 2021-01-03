package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Comparator;
import java.util.Optional;

@Singleton
public class ObjectiveFormatter implements Formatter<Objective> {

    @Override
    public TextComponent asText(final Objective value) {
        final Optional<Score> optionalBestScore = value.getScores()
                .values()
                .stream()
                .max(Comparator.comparingInt(Score::getScore));

        final TextComponent displayedAsText = Component.text("Display as: ", NamedTextColor.GRAY)
                .append(value.getDisplayName());

        final TextComponent displayModeText = Component.text("Display mode: ", NamedTextColor.GRAY)
                .append(Component.text(value.getDisplayMode().key(RegistryTypes.OBJECTIVE_DISPLAY_MODE).getValue(), NamedTextColor.GOLD));

        final TextComponent criterionText = Component.text("Criterion: ", NamedTextColor.GRAY)
                .append(Component.text(value.getCriterion().key(RegistryTypes.CRITERION).getValue(), NamedTextColor.GOLD));

        final TextComponent bestScoreText = optionalBestScore
                .map(bestScore -> Component.newline()
                        .append(Component.text("Best score: ", NamedTextColor.GRAY))
                        .append(Component.text(bestScore.getScore(), NamedTextColor.GOLD))
                        .append(Component.text(" from ", NamedTextColor.GRAY))
                        .append(bestScore.getName())
                )
                .orElse(Component.empty());

        final TextComponent registeredScoresText = Component.text("Registered scores: ", NamedTextColor.GRAY)
                .append(Component.text(value.getScores().size(), NamedTextColor.GOLD));

        final TextComponent hoverText = Component.text()
                .append(displayedAsText)
                .append(Component.newline())
                .append(displayModeText)
                .append(Component.newline())
                .append(criterionText)
                .append(Component.newline())
                .append(registeredScoresText)
                .append(bestScoreText)
                .build();

        return Component.text(value.getName())
                .hoverEvent(HoverEvent.showText(hoverText))
                .decoration(TextDecoration.UNDERLINED, true);
    }

}
