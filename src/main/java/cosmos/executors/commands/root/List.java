package cosmos.executors.commands.root;

import com.google.common.collect.Iterables;
import com.google.inject.Singleton;
import cosmos.constants.WorldStates;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.ValueParser;
import org.spongepowered.api.entity.Entity;

import java.util.stream.Collectors;

@Singleton
public class List extends AbstractCommand {

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.text("Click to teleport yourself (dangerously)"));
        final HoverEvent<Component> hoverEventSafely = HoverEvent.showText(Component.text("Click to teleport yourself (dangerously)"));
        final Component dashText = Component.text(" - ");
        final Component infoText = Component.text("Info", NamedTextColor.GRAY).decorate(TextDecoration.UNDERLINED);
        final Component moveText = Component.text("Move", NamedTextColor.GRAY).decorate(TextDecoration.UNDERLINED);
        final Component safeText = Component.text("Safe", NamedTextColor.GRAY).decorate(TextDecoration.UNDERLINED);

        final Iterable<Component> loadedWorlds = Sponge.getServer().getWorldManager().worlds()
                .stream()
                .map(world -> {
                    final ResourceKey worldKey = world.getKey();
                    final String moveCommand = super.serviceProvider.transportation()
                            .buildCommand(null, worldKey, null, null, false);
                    final String moveSafelyCommand = super.serviceProvider.transportation()
                            .buildCommand(null, worldKey, null, null, true);

                    return Component.text()
                            .append(
                                    super.serviceProvider.format().asText(worldKey, true).color(WorldStates.LOADED.getColor()),
                                    Component.text()
                                            .append(dashText)
                                            .append(infoText)
                                            .build(),
                                    Component.text()
                                            .append(dashText)
                                            .append(moveText)
                                            .hoverEvent(hoverEvent)
                                            .clickEvent(ClickEvent.suggestCommand(moveCommand))
                                            .build(),
                                    Component.text().append(dashText).append(safeText)
                                            .hoverEvent(hoverEventSafely)
                                            .clickEvent(ClickEvent.suggestCommand(moveSafelyCommand))
                                            .build()
                            )
                            .build();
                })
                .collect(Collectors.toList());

        final Iterable<Component> unloadedWorlds = super.serviceProvider.world().worldKeysOffline()
                .stream()
                .map(worldKey -> super.serviceProvider.format().asText(worldKey, true).color(WorldStates.UNLOADED.getColor()))
                .collect(Collectors.toList());

//        Iterable<Text> exportedWorlds = FinderWorldName.getExportedWorldNames()
//                .stream()
//                .filter(worldName -> bypassDim)
//                .map(worldName -> Text.builder(worldName).color(WorldStates.EXPORTED.getColor()).build())
//                .collect(Collectors.toList());

        final Iterable<Component> allWorlds = Iterables.concat(loadedWorlds, unloadedWorlds/*, exportedWorlds todo */);

        final TextComponent title = super.serviceProvider.message().getText(src, "success.root.list.header");

        super.serviceProvider.pagination().send(src, title, allWorlds, false);
    }

}
