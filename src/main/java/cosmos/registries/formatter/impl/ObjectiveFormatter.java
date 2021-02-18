package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.services.message.MessageService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
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
        final Optional<Score> optionalBestScore = value.scores()
                .values()
                .stream()
                .max(Comparator.comparingInt(Score::score));

        final boolean hasBestScore = optionalBestScore.isPresent();

        final TextComponent hoverText = this.messageService.getMessage(locale, "formatter.objective.hover")
                .replace("best1", hasBestScore ? optionalBestScore.get().score() : "none")
                .replace("best3", optionalBestScore.map(Score::name).orElse(Component.empty()))
                .replace("criterion", value.criterion().key(RegistryTypes.CRITERION))
                .replace("display", value.displayName())
                .replace("mode", value.displayMode().key(RegistryTypes.OBJECTIVE_DISPLAY_MODE))
                .replace("scores", value.scores().size())
                .condition("best2", hasBestScore)
                .condition("best3", hasBestScore)
                .gray()
                .asText();

        return Component.text(value.name()).hoverEvent(HoverEvent.showText(hoverText));
    }

}
