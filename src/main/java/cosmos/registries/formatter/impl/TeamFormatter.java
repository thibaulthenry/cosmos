package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.scoreboard.Team;

@Singleton
public class TeamFormatter implements Formatter<Team> {

    @Override
    public TextComponent asText(final Team value) {
        final TextComponent hoverText = Component.text("Displayed as: ", NamedTextColor.GRAY)
                .append(value.getDisplayName())
                .append(Component.newline())
                .append(Component.text("Registered members: "))
                .append(Component.text(value.getMembers().size(), NamedTextColor.GOLD))
                .append(Component.newline())
                .append(Component.text("Options: "))
                .append(Component.newline())
                .append(Component.text("   • Collision rule - "))
                //.append(Component.text(value.getCollisionRule().getKey().asString(), NamedTextColor.GOLD)) todo
                .append(Component.newline())
                .append(Component.text("   • Color - "))
                .append(Component.text(value.getColor().toString(), value.getColor()))
                .append(Component.newline())
                .append(Component.text("   • Death message visibility - "))
                //.append(Component.text(value.getDeathMessageVisibility().getKey().asString(), NamedTextColor.GOLD))
                .append(Component.newline())
                .append(Component.text("   • Allow friendly fire - "))
                .append(Component.text(value.allowFriendlyFire(), NamedTextColor.GOLD))
                .append(Component.newline())
                .append(Component.text("   • Nametag visibility - "))
                //.append(Component.text(value.getNameTagVisibility().getKey().asString(), NamedTextColor.GOLD))
                .append(Component.newline())
                .append(Component.text("   • Can see friendly invisibles - "))
                .append(Component.text(value.canSeeFriendlyInvisibles(), NamedTextColor.GOLD));

        return Component.text(value.getName())
                .hoverEvent(HoverEvent.showText(hoverText))
                .decoration(TextDecoration.UNDERLINED, true);
    }

}
