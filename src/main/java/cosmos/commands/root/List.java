package cosmos.commands.root;

import com.google.common.collect.Iterables;
import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.WorldStates;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.finders.FinderWorldName;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;
import java.util.stream.Collectors;

public class List extends AbstractCommand {

    public List() {
        super(
                GenericArguments.optional(
                        Arguments.enhancedCatalogedElement(ArgKeys.WORLD_DIMENSION, DimensionType.class)
                )
        );
    }

    @Override
    @SuppressWarnings("HardcodedFileSeparator")
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        Optional<DimensionType> optionalDimension = args.getOne(ArgKeys.WORLD_DIMENSION.t);
        boolean bypassDim = !optionalDimension.isPresent();
        DimensionType dim = optionalDimension.orElse(null);

        Text dash = Text.of(TextColors.WHITE, " - ");

        Iterable<Text> loadedWorlds = FinderWorldProperties.getLoadedWorldProperties()
                .stream()
                .filter(worldProperties -> bypassDim || worldProperties.getDimensionType().equals(dim))
                .map(worldProperties -> {
                    String worldName = worldProperties.getWorldName();

                    return Text.builder(worldName)
                            .color(WorldStates.LOADED.getColor())
                            .append(Text.builder()
                                    .append(dash)
                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Info"))
                                    .onHover(TextActions.showText(Information.getLoadedWorldInformation(worldProperties, WorldStates.LOADED)))
                                    .build()
                            )
                            .append(Text.builder()
                                    .append(dash)
                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Move"))
                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (dangerously)")))
                                    .onClick(TextActions.suggestCommand("/cm move " + worldName))
                                    .build()
                            )
                            .append(Text.builder()
                                    .append(dash)
                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Safe"))
                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (safely)")))
                                    .onClick(TextActions.suggestCommand("/cm move " + worldName + " --safe-only"))
                                    .build()
                            )
                            .build();
                })
                .collect(Collectors.toList());

        Iterable<Text> unloadedWorlds = FinderWorldProperties.getUnloadedWorldProperties(false)
                .stream()
                .filter(worldProperties -> bypassDim || worldProperties.getDimensionType().equals(dim))
                .map(worldProperties -> {
                    String worldName = worldProperties.getWorldName();

                    return Text.builder(worldName)
                            .color(WorldStates.UNLOADED.getColor())
                            .append(Text.builder()
                                    .append(dash)
                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Info"))
                                    .onHover(TextActions.showText(Information.getUnloadedWorldInformation(worldProperties, WorldStates.UNLOADED)))
                                    .build()
                            )
                            .append(Text.builder()
                                    .append(dash)
                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Move"))
                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (dangerously)")))
                                    .onClick(TextActions.suggestCommand("/cm move " + worldName))
                                    .build()
                            )
                            .append(Text.builder()
                                    .append(dash)
                                    .append(Text.of(TextStyles.UNDERLINE, TextColors.GRAY, "Safe"))
                                    .onHover(TextActions.showText(Text.of("Click to teleport yourself (safely)")))
                                    .onClick(TextActions.suggestCommand("/cm move " + worldName + " --safe-only"))
                                    .build()
                            )
                            .build();
                })
                .collect(Collectors.toList());

        Iterable<Text> disabledWorlds = FinderWorldName.getDisabledWorldNames()
                .stream()
                .filter(worldName -> {
                    if (bypassDim) return true;
                    Optional<WorldProperties> optionalWorldProperties = FinderWorldProperties.getDisabledWorldProperties(worldName);
                    return optionalWorldProperties.isPresent() && optionalWorldProperties.get().getDimensionType().equals(dim);
                })
                .map(worldName -> Text.builder(worldName).color(WorldStates.DISABLED.getColor()).build())
                .collect(Collectors.toList());

        Iterable<Text> exportedWorlds = FinderWorldName.getExportedWorldNames()
                .stream()
                .filter(worldName -> bypassDim)
                .map(worldName -> Text.builder(worldName).color(WorldStates.EXPORTED.getColor()).build())
                .collect(Collectors.toList());

        Iterable<Text> allWorlds = Iterables.concat(loadedWorlds, unloadedWorlds, disabledWorlds, exportedWorlds);

        Text title = Text.builder().append(
                Text.of("Worlds ["),
                WorldStates.LOADED.toText(), Text.of(" - "),
                WorldStates.UNLOADED.toText(), Text.of(" - "),
                WorldStates.DISABLED.toText(), Text.of(" - "),
                WorldStates.EXPORTED.toText(), Text.of("]")
        ).build();

        sendPaginatedOutput(src, title, allWorlds, false);
    }
}
