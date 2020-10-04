package cosmos.commands.properties;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

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

        if (optionalGameMode.isPresent()) {
            gameMode = optionalGameMode.get();
            mutableText = "successfully";
            worldProperties.setGameMode(gameMode);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.GAME_MODE.asText(worldProperties, mutableText, gameMode));
    }
}
