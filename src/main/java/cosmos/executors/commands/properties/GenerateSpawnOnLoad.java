package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Singleton
public class GenerateSpawnOnLoad extends AbstractPropertiesCommand {

    public GenerateSpawnOnLoad() {
        super(Parameter.bool().setKey(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties) throws CommandException {
        final Optional<Boolean> optionalInput = context.getOne(CosmosKeys.STATE);
        boolean value = properties.doesGenerateSpawnOnLoad();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setGenerateSpawnOnLoad(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.generate-spawn-on-load.set" : "success.properties.generate-spawn-on-load.get")
                .replace("world", properties)
                .condition("value", value)
                .successColor()
                .sendTo(src);
    }
}
