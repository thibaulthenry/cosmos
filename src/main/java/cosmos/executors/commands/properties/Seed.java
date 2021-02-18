package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class Seed extends AbstractPropertiesCommand {

    public Seed() {
        super(Parameter.longNumber().key(CosmosKeys.SEED).optional().build());
    }

    @Override
    protected void run(Audience src, CommandContext context, ServerWorldProperties properties) throws CommandException {
        final Optional<Long> optionalInput = context.one(CosmosKeys.SEED);
        long value = properties.worldGenerationConfig().seed();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.worldGenerationConfig().setSeed(value);
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.seed.set" : "success.properties.seed.get")
                .replace("value", value)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
