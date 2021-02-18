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
import org.spongepowered.api.scoreboard.Team;

import java.util.Locale;

@Singleton
public class TeamFormatter implements LocaleFormatter<Team> {

    private final MessageService messageService;

    @Inject
    public TeamFormatter(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public TextComponent asText(final Team value, final Locale locale) {
        final TextComponent hoverText = this.messageService.getMessage(locale, "formatter.team.hover")
                .replace("collision", value.collisionRule().key(RegistryTypes.COLLISION_RULE))
                .replace("color", Component.text(value.color().toString(), value.color()))
                .replace("death", value.deathMessageVisibility().key(RegistryTypes.VISIBILITY))
                .replace("display", value.displayName())
                .replace("members", value.members().size())
                .replace("prefix", value.prefix())
                .replace("suffix", value.suffix())
                .replace("tag", value.nameTagVisibility().key(RegistryTypes.VISIBILITY))
                .condition("fire", value.allowFriendlyFire())
                .condition("invisibles", value.canSeeFriendlyInvisibles())
                .gray()
                .asText();

        return Component.text(value.name()).hoverEvent(HoverEvent.showText(hoverText));
    }

}
