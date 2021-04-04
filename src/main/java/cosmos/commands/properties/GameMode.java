package cosmos.commands.properties;

import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ListenerRegister;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.finders.FinderWorldProperties;
import cosmos.statics.handlers.OutputFormatter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameMode extends AbstractPropertyCommand {

    public GameMode() {
        super(
                GenericArguments.optional(
                        Arguments.enhancedCatalogedElement(
                                ArgKeys.VALUE,
                                org.spongepowered.api.entity.living.player.gamemode.GameMode.class
                        )
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<org.spongepowered.api.entity.living.player.gamemode.GameMode> optionalGameMode = args.getOne(ArgKeys.VALUE.t);
        org.spongepowered.api.entity.living.player.gamemode.GameMode gameMode = worldProperties.getGameMode();
        String mutableText = "currently";

        if (!optionalGameMode.isPresent()) {
            src.sendMessage(Outputs.GAME_MODE.asText(worldProperties, mutableText, gameMode));
            return;
        }

        gameMode = optionalGameMode.get();
        mutableText = "successfully";

        Collection<WorldProperties> group = GroupRegister.find(Tuple.of(PerWorldCommands.GAME_MODES, worldProperties.getWorldName()))
                .orElse(Collections.singleton(worldProperties.getWorldName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(World::getProperties)
                .collect(Collectors.toList());

        for (WorldProperties properties : group) {
            properties.setGameMode(gameMode);
            FinderWorldProperties.saveProperties(properties);
            ListenerRegister.triggerToggleListener(PerWorldCommands.GAME_MODES.getListenerClass(), true);
        }

        if (group.size() > 1) {
            Collection<Text> formattedWorlds = group.stream()
                    .map(OutputFormatter::formatOutput)
                    .map(Text::of)
                    .collect(Collectors.toList());

            src.sendMessage(Outputs.GAME_MODE_GROUP.asText(Text.joinWith(Text.of(TextColors.GRAY, ", "), formattedWorlds), mutableText, gameMode));
        } else {
            src.sendMessage(Outputs.GAME_MODE.asText(worldProperties, mutableText, gameMode));
        }
    }

}
