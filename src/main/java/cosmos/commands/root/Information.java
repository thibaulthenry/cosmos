package cosmos.commands.root;

import com.google.common.collect.Iterables;
import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.WorldStates;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.arguments.WorldPropertiesArguments;
import cosmos.statics.finders.FinderWorldProperties;
import cosmos.statics.handlers.OutputFormatter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class Information extends AbstractCommand {

    public Information() {
        super(
                Arguments.limitCompleteElement(
                        GenericArguments.firstParsing(
                                WorldPropertiesArguments.loadedChoices(ArgKeys.LOADED_WORLD),
                                WorldPropertiesArguments.unloadedChoices(ArgKeys.UNLOADED_WORLD, false),
                                WorldNameArguments.disabledChoices(ArgKeys.DISABLED_WORLD)
                        )
                )
        );
    }

    public static Text getLoadedWorldInformation(WorldProperties worldProperties, WorldStates worldStates) {
        Optional<World> optionalWorld = Sponge.getServer().getWorld(worldProperties.getUniqueId());

        Text loadedWorldInformation = optionalWorld
                .map(world -> Text.of(
                        node("Living entities", world.getEntities().size()),
                        node("Tile entities", world.getTileEntities().size()),
                        node("Players", world.getPlayers().size()),
                        node("Loaded chunks", Iterables.size(world.getLoadedChunks()))
                ))
                .orElse(Text.EMPTY);

        return Text.of(
                getUnloadedWorldInformation(worldProperties, worldStates),
                loadedWorldInformation
        );
    }

    public static Text getUnloadedWorldInformation(WorldProperties worldProperties, WorldStates worldStates) {
        Text yes = Text.of(TextColors.GREEN, TextStyles.BOLD, TextStyles.ITALIC, "✓");
        Text no = Text.of(TextColors.RED, TextStyles.BOLD, "✗");

        return Text.of(
                getDisabledWorldInformation(worldProperties, worldStates),
                node("Generator type", OutputFormatter.formatOutput(worldProperties.getGeneratorType())),
                node("Loads on startup", worldProperties.loadOnStartup() ? yes : no),
                node("Keeps spawn loaded", worldProperties.doesKeepSpawnLoaded() ? yes : no),
                node("Generates spawn on load", worldProperties.doesGenerateSpawnOnLoad() ? yes : no),
                node("Structures", worldProperties.usesMapFeatures() ? yes : no),
                node("PvP", worldProperties.isPVPEnabled() ? yes : no),
                node("Hardcore", worldProperties.isHardcore() ? yes : no),
                node("Command blocks", worldProperties.areCommandsAllowed() ? yes : no)
        );
    }



    private static Text getDisabledWorldInformation(WorldProperties worldProperties, WorldStates worldStates) {
        return Text.of(
                TextColors.BLUE, "Showing world information about ", TextColors.WHITE, worldProperties.getWorldName(),
                node("State", worldStates.toText()),
                node("UUID", worldProperties.getUniqueId()),
                node("Seed", worldProperties.getSeed()),
                node("Dimension", OutputFormatter.formatOutput(worldProperties.getDimensionType())),
                node("Spawn position", OutputFormatter.formatOutput(worldProperties.getSpawnPosition())),
                node("Difficulty", OutputFormatter.formatOutput(worldProperties.getDifficulty())),
                node("Game mode", OutputFormatter.formatOutput(worldProperties.getGameMode()))
        );
    }

    private static Text node(String label, Object value, boolean breakLine) {
        return Text.of(
                Text.NEW_LINE, breakLine ? Text.NEW_LINE : Text.EMPTY, TextColors.WHITE, " • ",
                TextColors.GRAY, label, ": ", TextColors.GREEN, value
        );
    }

    private static Text node(String label, Object value) {
        return node(label, value, false);
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        Text worldInformation = args.<WorldProperties>getOne(ArgKeys.LOADED_WORLD.t)
                .map(worldProperties -> getLoadedWorldInformation(worldProperties, WorldStates.LOADED))
                .map(Optional::of)
                .orElse(
                        args.<WorldProperties>getOne(ArgKeys.UNLOADED_WORLD.t)
                                .map(worldProperties -> getUnloadedWorldInformation(worldProperties, WorldStates.UNLOADED))
                )
                .map(Optional::of)
                .orElse(
                        args.<String>getOne(ArgKeys.DISABLED_WORLD.t)
                                .flatMap(FinderWorldProperties::getDisabledWorldProperties)
                                .map(worldProperties -> getDisabledWorldInformation(worldProperties, WorldStates.DISABLED))
                )
                .orElseThrow(Outputs.INVALID_ALL_WORLD_CHOICE.asSupplier());

        src.sendMessage(worldInformation);
    }

}
