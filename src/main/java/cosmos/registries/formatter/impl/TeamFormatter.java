package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.services.message.MessageService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
                .replace("collision", value.getCollisionRule().key(RegistryTypes.COLLISION_RULE).getValue())
                .replace("color", "N/A") // todo .append(Component.text(value.getColor().toString(), value.getColor()))
                .replace("death", value.getDeathMessageVisibility().key(RegistryTypes.VISIBILITY).getValue())
                .replace("display", value.getDisplayName())
                .replace("members", value.getMembers().size())
                .replace("prefix", value.getPrefix())
                .replace("suffix", value.getSuffix())
                .replace("tag", value.getNameTagVisibility().key(RegistryTypes.VISIBILITY).getValue())
                .condition("fire", value.allowFriendlyFire())
                .condition("invisibles", value.canSeeFriendlyInvisibles())
                .gray()
                .asText();

        return Component.text(value.getName()).hoverEvent(HoverEvent.showText(hoverText));
    }

}
