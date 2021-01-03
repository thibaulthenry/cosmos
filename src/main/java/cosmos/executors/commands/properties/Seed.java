package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class Seed extends AbstractPropertiesCommand {

    public Seed() {
        super(Parameter.longNumber().setKey(CosmosKeys.SEED).optional().build());
    }

    @Override
    protected void run(Audience src, CommandContext context, ServerWorldProperties properties) throws CommandException {
        final Optional<Long> optionalInput = context.getOne(CosmosKeys.SEED);
        long value = properties.getWorldGenerationSettings().getSeed();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.getWorldGenerationSettings().setSeed(value);
            this.serviceProvider.world().saveProperties(src, properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.seed.set" : "success.properties.seed.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
