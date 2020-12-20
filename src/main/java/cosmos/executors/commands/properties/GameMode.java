package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Singleton
public class GameMode extends AbstractPropertiesCommand {

    public GameMode() {
        super(CosmosParameters.GAME_MODE_OPTIONAL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties) throws CommandException {
        final Optional<org.spongepowered.api.entity.living.player.gamemode.GameMode> optionalInput = context.getOne(CosmosParameters.GAME_MODE_OPTIONAL);
        org.spongepowered.api.entity.living.player.gamemode.GameMode value = properties.getGameMode();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setGameMode(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.game-mode.set" : "success.properties.game-mode.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
