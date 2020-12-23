package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class Difficulty extends AbstractPropertiesCommand {

    public Difficulty() {
        super(CosmosParameters.DIFFICULTY_OPTIONAL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<org.spongepowered.api.world.difficulty.Difficulty> optionalInput = context.getOne(CosmosParameters.DIFFICULTY_OPTIONAL);
        org.spongepowered.api.world.difficulty.Difficulty value = properties.getDifficulty();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setDifficulty(value);
            this.serviceProvider.worldProperties().save(properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.difficulty.set" : "success.properties.difficulty.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
