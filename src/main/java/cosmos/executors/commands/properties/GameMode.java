package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class GameMode extends AbstractPropertiesCommand {

    public GameMode() {
        super(
                Parameter.registryElement(TypeToken.get(org.spongepowered.api.entity.living.player.gamemode.GameMode.class), RegistryTypes.GAME_MODE, ResourceKey.SPONGE_NAMESPACE)
                        .key(CosmosKeys.GAME_MODE)
                        .optional()
                        .build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<org.spongepowered.api.entity.living.player.gamemode.GameMode> optionalInput = context.one(CosmosKeys.GAME_MODE);
        org.spongepowered.api.entity.living.player.gamemode.GameMode value = properties.gameMode();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setGameMode(value);
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.game-mode.set" : "success.properties.game-mode.get")
                .replace("value", value.key(RegistryTypes.GAME_MODE))
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
