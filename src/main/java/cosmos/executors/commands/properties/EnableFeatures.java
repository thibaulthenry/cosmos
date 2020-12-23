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
public class EnableFeatures extends AbstractPropertiesCommand {

    public EnableFeatures() {
        super(Parameter.bool().setKey(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<Boolean> optionalInput = context.getOne(CosmosKeys.STATE);
        boolean value = properties.getWorldGenerationSettings().doFeaturesGenerate();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.getWorldGenerationSettings().setFeaturesGenerate(value);
            this.serviceProvider.worldProperties().save(properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.enable-structures.set" : "success.properties.enable-structures.get")
                .replace("world", properties)
                .condition("value", value)
                .successColor()
                .sendTo(src);
    }
}
