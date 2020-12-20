package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Singleton
public class GeneratorModifierType extends AbstractPropertiesCommand {

    public GeneratorModifierType() {
        super(CosmosParameters.GENERATOR_MODIFIER_TYPE_OPTIONAL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties) throws CommandException {
        final Optional<org.spongepowered.api.world.gen.GeneratorModifierType> optionalInput = context.getOne(CosmosParameters.GENERATOR_MODIFIER_TYPE_OPTIONAL);
        org.spongepowered.api.world.gen.GeneratorModifierType value = properties.getGeneratorModifierType();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setGeneratorModifierType(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.generator-modifier-type.set" : "success.properties.generator-modifier-type.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
