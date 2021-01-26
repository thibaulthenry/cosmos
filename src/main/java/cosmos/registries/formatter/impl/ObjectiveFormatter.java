package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.services.message.MessageService;
import cosmos.services.transportation.TransportationService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;

@Singleton
public class ObjectiveFormatter implements LocaleFormatter<Objective> {

    private final MessageService messageService;

    @Inject
    public ObjectiveFormatter(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public TextComponent asText(final Objective value, final Locale locale) {
        final Optional<Score> optionalBestScore = value.getScores()
                .values()
                .stream()
                .max(Comparator.comparingInt(Score::getScore));

        final boolean hasBestScore = optionalBestScore.isPresent();

        final TextComponent hoverText = this.messageService.getMessage(locale, "formatter.objective.hover")
                .replace("best1", hasBestScore ? optionalBestScore.get().getScore() : "none")
                .replace("best3", optionalBestScore.map(Score::getName).orElse(Component.empty()))
                .replace("criterion", value.getCriterion().key(RegistryTypes.CRITERION).getValue())
                .replace("display", value.getDisplayName())
                .replace("mode", value.getDisplayMode().key(RegistryTypes.OBJECTIVE_DISPLAY_MODE).getValue())
                .replace("scores", value.getScores().size())
                .condition("best2", hasBestScore)
                .condition("best3", hasBestScore)
                .gray()
                .asText();

        // todo add display slot

        return Component.text(value.getName()).hoverEvent(HoverEvent.showText(hoverText));
    }

}
