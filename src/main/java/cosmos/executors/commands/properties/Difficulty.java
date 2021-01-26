package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class Difficulty extends AbstractPropertiesCommand {

    public Difficulty() {
        super(CosmosParameters.DIFFICULTY_OPTIONAL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<org.spongepowered.api.world.difficulty.Difficulty> optionalInput = context.getOne(CosmosParameters.DIFFICULTY_OPTIONAL);
        org.spongepowered.api.world.difficulty.Difficulty value = properties.difficulty();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setDifficulty(value);
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.difficulty.set" : "success.properties.difficulty.get")
                .replace("value", value.key(RegistryTypes.DIFFICULTY))
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
