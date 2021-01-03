package cosmos.executors.commands.root;

import com.google.common.collect.Iterables;
import com.google.inject.Singleton;
import cosmos.constants.WorldStates;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.stream.Collectors;

@Singleton
public class List extends AbstractCommand {

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final Iterable<Component> loadedWorlds = Sponge.getServer().getWorldManager().worlds()
                .stream()
                //.filter(world -> bypassDim || world.getGenerator().biomeProvider().equals(BiomeProvider))
                .map(world -> {
                    final ResourceKey worldKey = world.getKey();

                    return Component.text(worldKey.getFormatted(), WorldStates.LOADED.getColor());
//                            .append(Text.builder()
//                                    .append(dash)
//                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Info"))
//                                    .onHover(TextActions.showText(Information.getLoadedWorldInformation(worldProperties, WorldStates.LOADED)))
//                                    .build()
//                            )
//                            .append(Text.builder()
//                                    .append(dash)
//                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Move"))
//                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (dangerously)")))
//                                    .onClick(TextActions.suggestCommand("/cm move " + worldKey))
//                                    .build()
//                            )
//                            .append(Text.builder()
//                                    .append(dash)
//                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Safe"))
//                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (safely)")))
//                                    .onClick(TextActions.suggestCommand("/cm move " + worldKey + " --safe-only"))
//                                    .build()
//                            )
                })
                .collect(Collectors.toList());

        final Iterable<Component> unloadedWorlds = this.serviceProvider.world().worldKeysOffline()
                .stream()
                //.filter(worldProperties -> bypassDim || worldProperties.getDimensionType().equals(dim))
                .map(worldKey -> Component.text(worldKey.getFormatted(), WorldStates.UNLOADED.getColor()))
//                            .append(Text.builder()
//                                    .append(dash)
//                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Info"))
//                                    .onHover(TextActions.showText(Information.getUnloadedWorldInformation(worldProperties, WorldStates.UNLOADED)))
//                                    .build()
//                            )
//                            .append(Text.builder()
//                                    .append(dash)
//                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Move"))
//                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (dangerously)")))
//                                    .onClick(TextActions.suggestCommand("/cm move " + worldName))
//                                    .build()
//                            )
//                            .append(Text.builder()
//                                    .append(dash)
//                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Safe"))
//                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (safely)")))
//                                    .onClick(TextActions.suggestCommand("/cm move " + worldName + " --safe-only"))
//                                    .build()
//                            )
//                            .build();
//                })
                .collect(Collectors.toList());

//        Iterable<Text> exportedWorlds = FinderWorldName.getExportedWorldNames() todo
//                .stream()
//                .filter(worldName -> bypassDim)
//                .map(worldName -> Text.builder(worldName).color(WorldStates.EXPORTED.getColor()).build())
//                .collect(Collectors.toList());

        final Iterable<Component> allWorlds = Iterables.concat(loadedWorlds, unloadedWorlds/*, exportedWorlds*/); // todo

        final TextComponent title = this.serviceProvider.message().getText(src, "error.invalid.value");

        this.serviceProvider.pagination().send(src, title, allWorlds, false);
    }

}
